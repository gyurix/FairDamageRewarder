package FairDamageRewarder;

import PluginReference.MC_DamageType;
import PluginReference.MC_Entity;
import PluginReference.MC_EntityType;
import PluginReference.MC_EventInfo;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.PluginBase;
import PluginReference.PluginInfo;
import WrapperObjects.Entities.EntityWrapper;
import WrapperObjects.Entities.PlayerWrapper;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangFile;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import joebkt.EntityGeneric;
import joebkt.EntityPlayer;

public class MyPlugin extends PluginBase
{
  EnumMap<MC_EntityType, Double> rewardmin = new EnumMap(MC_EntityType.class);
  EnumMap<MC_EntityType, Double> rewardmax = new EnumMap(MC_EntityType.class);
  HashMap<EntityGeneric, Data> entities = new HashMap();
  public static long tracktime;
  public static Random rand = new Random();
  ConfigFile kf;

  public void onServerFullyLoaded()
  {
    String dir = KFA.fileCopy(this, "config.yml", false);
    this.kf = new ConfigFile(dir + "/" + "config.yml");
    this.kf.repair_missing = true;
    KFA.lf.insert(new LangFile(KFA.getFileStream(this, "lang.lng")));
    tracktime = this.kf.getLong("tracktime");
    for (MC_EntityType type : MC_EntityType.values()) {
      String s = this.kf.get("rewards." + type.name(), "0");
      try {
        String[] s2 = s.split("\\ ", 2);
        double min = Double.valueOf(s2[0]).doubleValue();
        this.rewardmin.put(type, Double.valueOf(min));
        try {
          this.rewardmax.put(type, Double.valueOf(s2[1]));
        }
        catch (Throwable e) {
          this.rewardmax.put(type, Double.valueOf(min));
        }
      }
      catch (Throwable e) {
        System.out.println("[DamageHandler] Error the reward for killing " + type.name() + " entity is missing from config.");
        this.rewardmin.put(type, Double.valueOf(0.0D));
        this.rewardmax.put(type, Double.valueOf(0.0D));
      }
    }
  }

  public PluginInfo getPluginInfo() {
    PluginInfo inf = new PluginInfo();
    inf.name = "DamageHandler";
    inf.eventSortOrder = 1000000000.0D;
    inf.description = "The plugin for handling and rewarding all the damage correctly. By Gyurix";
    inf.ref = this;
    inf.version = "1.2.0";
    return inf;
  }

  public void onAttemptEntityDamage(MC_Entity entDmg, MC_DamageType dmgType, double amt, MC_EventInfo ei)
  {
    if (!ei.isCancelled) {
      amt = amt < entDmg.getHealth() ? amt : entDmg.getHealth();
      EntityGeneric ent = ((EntityWrapper)entDmg).ent;
      Data d = (Data)this.entities.get(ent);
      Attack a = new Attack(dmgType, entDmg.getAttacker(), amt);
      if (d == null) {
        this.entities.put(ent, new Data(a));
      }
      else
        d.addAttack(a);
    }
  }

  public void rewardPlayers(MC_Entity entVictim, Data d) {
    double min = ((Double)this.rewardmin.get(entVictim.getType())).doubleValue();
    double max = ((Double)this.rewardmax.get(entVictim.getType())).doubleValue();
    double rew = min == max ? min : min + (max - min) * rand.nextDouble();
    MC_Player plr;
    if ((entVictim instanceof MC_Player)) {
      for (Map.Entry e : d.getPercentages().entrySet()) {
        plr = KFA.srv.getOnlinePlayerByName((String)e.getKey());
        plr.setEconomyBalance(plr.getEconomyBalance() + ((Double)e.getValue()).doubleValue() * rew);
        ChatAPI.msg(plr, "damagehandler." + (rew < 0.0D ? "punish" : "reward") + "ed.player", new String[] { 
          "<money>", round(rew < 0.0D ? 0.0D - ((Double)e.getValue()).doubleValue() * rew : ((Double)e.getValue()).doubleValue() * rew), 
          "<percent>", round(((Double)e.getValue()).doubleValue() * 100.0D), 
          "<playername>", entVictim.getName() });
      }
    }
    else {
      String entName = entVictim.getType().name().toLowerCase();
      for (Map.Entry e : d.getPercentages().entrySet()) {
        MC_Player plr = KFA.srv.getOnlinePlayerByName((String)e.getKey());
        if (plr.hasPermission("damagehandler.reward." + entName)) {
          plr.setEconomyBalance(plr.getEconomyBalance() + ((Double)e.getValue()).doubleValue() * rew);
          ChatAPI.msg(plr, "damagehandler." + (rew < 0.0D ? "punish" : "reward") + "ed.entity", new String[] { 
            "<money>", round(rew < 0.0D ? 0.0D - rew * ((Double)e.getValue()).doubleValue() : rew * ((Double)e.getValue()).doubleValue()), 
            "<percent>", round(((Double)e.getValue()).doubleValue() * 100.0D), 
            "<entity>", KFA.l(plr, "damagehandler.entity." + entName) });
        }
      }
    }
  }

  public double round(double input) { return Math.round(input * 100.0D) / 100.0D; }


  public void onAttemptDeath(MC_Entity entVictim, MC_Entity entKiller, MC_DamageType dmgType, float dmgAmount)
  {
    Data d = (Data)this.entities.get(((EntityWrapper)entVictim).ent);
    d.cleanup();
    if (!d.containsPlayerDamage())
      return;
    rewardPlayers(entVictim, d);
    double totaldmg = d.getAllDmg();
    Set particulars = d.getParticularPlayers();
    for (MC_Player plr : KFA.srv.getPlayers()) {
      if (particulars.contains(plr.getName())) { if (plr.hasPermission("damagehandler.particularreport." + (entVictim.getType() == MC_EntityType.PLAYER ? "player" : 
          new StringBuilder("entity.").append(entVictim.getType().name().toLowerCase()).toString())));
      }
      else if (!plr.hasPermission("damagehandler.seereport." + (entVictim.getType() == MC_EntityType.PLAYER ? "player" : 
          new StringBuilder("entity.").append(entVictim.getType().name().toLowerCase()).toString())))
          continue; if (entVictim.getType() == MC_EntityType.PLAYER)
        ChatAPI.msg(plr, "damagehandler.died.player", new String[] { "<playername>", entVictim.getName() });
      else
        ChatAPI.msg(plr, "damagehandler.died.entity", new String[] { "<entity>", KFA.l(plr, "damagehandler.entity." + entVictim.getType().name().toLowerCase()) });
      for (Attack a : d.attacks) {
        if (a.ent == null) {
          ChatAPI.msg(plr, "damagehandler.report.nocauser", new String[] { 
            "<type>", KFA.l(plr, "damagehandler.type." + a.dt.name().toLowerCase()), 
            "<damage>", round(a.dmg), 
            "<percent>", round(a.dmg / totaldmg * 100.0D) });
        }
        else if ((a.ent instanceof EntityPlayer)) {
          ChatAPI.msg(plr, "damagehandler.report.player", new String[] { 
            "<type>", KFA.l(plr, "damagehandler.type." + a.dt.name().toLowerCase()), 
            "<damage>", round(a.dmg), 
            "<percent>", round(a.dmg / totaldmg * 100.0D), 
            "<playername>", new PlayerWrapper((EntityPlayer)a.ent).getName() });
        }
        else {
          ChatAPI.msg(plr, "damagehandler.report.entity", new String[] { 
            "<type>", KFA.l(plr, "damagehandler.type." + a.dt.name().toLowerCase()), 
            "<damage>", round(a.dmg), 
            "<percent>", round(a.dmg / totaldmg * 100.0D), 
            "<entity>", KFA.l(plr, "damagehandler.entity." + new EntityWrapper(a.ent).getType().name().toLowerCase()) });
        }
      }
    }

    this.entities.remove(((EntityWrapper)entVictim).ent);
  }
}

/* Location:           C:\Users\Gyuri\Downloads\FairDamageRewarder.jar
 * Qualified Name:     FairDamageRewarder.MyPlugin
 * JD-Core Version:    0.6.2
 */
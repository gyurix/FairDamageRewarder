package FairDamageRewarder;

import PluginReference.MC_Player;
import WrapperObjects.Entities.PlayerWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import joebkt.EntityGeneric;
import joebkt.EntityPlayer;

public class Data
{
  final List<Attack> attacks = new ArrayList();

  public Data(Attack firstAttack) { this.attacks.add(firstAttack); }

  public void addAttack(Attack a) {
    if ((!this.attacks.isEmpty()) && (((Attack)this.attacks.get(this.attacks.size() - 1)).isSameAttacker(a))) {
      ((Attack)this.attacks.get(this.attacks.size() - 1)).dmg += a.dmg;
    }
    else
      this.attacks.add(a);
  }

  public boolean containsPlayerDamage() {
    for (Attack a : this.attacks) {
      if ((a.ent != null) && ((a.ent instanceof EntityPlayer))) {
        return true;
      }
    }
    return false;
  }
  public Set<String> getParticularPlayers() {
    Set out = new HashSet();
    for (Attack a : this.attacks) {
      if ((a.ent != null) && ((a.ent instanceof EntityPlayer))) {
        out.add(new PlayerWrapper((EntityPlayer)a.ent).getName());
      }
    }
    return out;
  }

  public void cleanup() {
    long time = System.currentTimeMillis() - MyPlugin.tracktime;
    for (Attack a : this.attacks)
      if (a.time < time)
        this.attacks.remove(a);
  }

  public double getAllDmg() {
    double out = 0.0D;
    for (Attack a : this.attacks) {
      out += a.dmg;
    }
    return out;
  }
  public double getAllDmg(String pln) {
    double out = 0.0D;
    for (Attack a : this.attacks) {
      if ((a.ent != null) && ((a.ent instanceof MC_Player)) && (a.ent.getName().equals(pln))) {
        out += a.dmg;
      }
    }
    return out;
  }
  public HashMap<String, Double> getDamages() {
    HashMap out = new HashMap();
    for (Attack a : this.attacks) {
      if ((a.ent != null) && ((a.ent instanceof EntityPlayer))) {
        Double dmgo = (Double)out.get(a.ent.getName());
        double dmg = dmgo == null ? 0.0D : dmgo.doubleValue();
        out.put(a.ent.getName(), Double.valueOf(dmg + a.dmg));
      }
    }
    return out;
  }
  public HashMap<String, Double> getPercentages() {
    double alldmg = getAllDmg();
    HashMap out = getDamages();
    for (Map.Entry e : out.entrySet()) {
      e.setValue(Double.valueOf(((Double)e.getValue()).doubleValue() / alldmg));
    }
    return out;
  }
}

/* Location:           C:\Users\Gyuri\Downloads\FairDamageRewarder.jar
 * Qualified Name:     FairDamageRewarder.Data
 * JD-Core Version:    0.6.2
 */
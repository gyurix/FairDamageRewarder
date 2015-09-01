package FairDamageRewarder;

import PluginReference.MC_DamageType;
import PluginReference.MC_Entity;
import WrapperObjects.Entities.EntityWrapper;
import joebkt.EntityGeneric;

public class Attack
{
  MC_DamageType dt;
  EntityGeneric ent;
  long time;
  double dmg;

  public Attack(MC_DamageType damagetype, MC_Entity causer, double damage)
  {
    this.dt = damagetype;
    this.ent = (causer == null ? null : ((EntityWrapper)causer).ent);
    this.dmg = damage;
    this.time = System.currentTimeMillis();
  }
  public boolean isSameAttacker(Attack a) {
    return (this.dt == a.dt) && (this.ent == a.ent);
  }
}

/* Location:           C:\Users\Gyuri\Downloads\FairDamageRewarder.jar
 * Qualified Name:     FairDamageRewarder.Attack
 * JD-Core Version:    0.6.2
 */
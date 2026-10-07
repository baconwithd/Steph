/*
 * Decompiled with CFR 0.152.
 */
package ichttt.mods.firstaid.survival;

import ichttt.mods.firstaid.compat.lrtactical.MedicalBridge;

public final class ClinicalState {
    public static final int DAY_TICKS = 24000;
    public static final int MAX_AWAKE_TICKS = 144000;
    public static final int SLEEP_TICKS = 160;
    public static final int EXTRACTION_TICKS = 80;
    public static final int GUNSHOT_HEAL_TICKS = 12000;
    public static final int CUT_HEAL_TICKS = 2400;
    public final Wound[] wounds = new Wound[8];
    public long awakeTicks;
    public int sleepTicks;
    public int mentalDamageTicks;
    public int warningStage;
    private boolean headDamageDue;
    public int extractionPart = -1;
    public int extractionProgress;
    public int dressingPart = -1;
    public int dressingProgress;
    public int jobHand;
    public int hygieneTick = Integer.MIN_VALUE;
    public int lastPlayerTick = Integer.MIN_VALUE;
    public Object jobItem;
    public int lastBulletId = Integer.MIN_VALUE;
    public int lastBulletTick = Integer.MIN_VALUE;

    public ClinicalState() {
        int n = 0;
        while (n < this.wounds.length) {
            this.wounds[n] = new Wound();
            ++n;
        }
    }

    public double sanity() {
        return Math.max(0.0, 100.0 * (1.0 - (double)Math.min(144000L, this.awakeTicks) / 144000.0));
    }

    public void tickSleep(boolean bl) {
        if (bl) {
            this.sleepTicks = Math.min(160, this.sleepTicks + 1);
            if (this.sleepTicks >= 160) {
                this.awakeTicks = 0L;
                this.mentalDamageTicks = 0;
                this.warningStage = 0;
                this.headDamageDue = false;
            }
        } else {
            this.sleepTicks = 0;
            this.awakeTicks = Math.min(144000L, this.awakeTicks + 1L);
            if (this.awakeTicks >= 144000L && ++this.mentalDamageTicks >= 600) {
                this.mentalDamageTicks = 0;
                this.headDamageDue = true;
            }
        }
    }

    public boolean takeHeadDamageDue() {
        boolean bl = this.headDamageDue;
        this.headDamageDue = false;
        return bl;
    }

    public int warningDue() {
        int n;
        n = this.awakeTicks >= 144000L ? 4 : (this.awakeTicks >= 138000L ? 3 : (this.awakeTicks >= 120000L ? 2 : (this.awakeTicks >= 96000L ? 1 : 0)));
        if (n > this.warningStage) {
            this.warningStage = n;
            return n;
        }
        return 0;
    }

    public boolean sleepComplete() {
        return this.sleepTicks >= 160;
    }

    public boolean startExtraction(int n) {
        if (n < 0 || n >= 8 || this.wounds[n].bullets <= 0 || this.extractionPart >= 0 || this.dressingPart >= 0) {
            return false;
        }
        this.extractionPart = n;
        this.extractionProgress = 0;
        return true;
    }

    public boolean startDressing(int n) {
        if (n < 0 || n >= 8 || !this.wounds[n].canBandage() || this.extractionPart >= 0 || this.dressingPart >= 0) {
            return false;
        }
        this.dressingPart = n;
        this.dressingProgress = 0;
        return true;
    }

    public void cancelJob() {
        this.cancelExtraction();
        this.dressingPart = -1;
        this.dressingProgress = 0;
        this.jobItem = null;
    }

    public boolean tickDressing(boolean bl) {
        if (this.dressingPart < 0) {
            return false;
        }
        if (!bl || !this.wounds[this.dressingPart].canBandage()) {
            this.cancelJob();
            return false;
        }
        if (++this.dressingProgress < 66) {
            return false;
        }
        this.wounds[this.dressingPart].bandage();
        this.dressingPart = -1;
        this.dressingProgress = 0;
        return true;
    }

    public void cancelExtraction() {
        this.extractionPart = -1;
        this.extractionProgress = 0;
    }

    public boolean tickExtraction(boolean bl) {
        if (this.extractionPart < 0) {
            return false;
        }
        if (!bl || this.wounds[this.extractionPart].bullets <= 0) {
            this.cancelExtraction();
            return false;
        }
        if (++this.extractionProgress < 80) {
            return false;
        }
        Wound wound = this.wounds[this.extractionPart];
        --wound.bullets;
        wound.bandaged = false;
        wound.lodgedOutcome = 0;
        wound.recovered = false;
        wound.gunshot = true;
        wound.healingLeft = 12000;
        this.cancelExtraction();
        return true;
    }

    public static final class Wound {
        public int bullets;
        public boolean gunshot;
        public boolean cut;
        public boolean bandaged;
        public int healingLeft;
        public boolean bitten;
        public boolean recovered;
        public int dressingAge;
        public boolean dirty;
        public boolean infected;
        public int deteriorationTicks;
        public int lodgedOutcome; // 0: unrolled; 1: recovery; 2: blocked

        public boolean active() {
            return this.gunshot || this.cut || this.bitten || this.infected || this.bullets > 0;
        }

        public void shot() {
            this.bullets = Math.min(99, this.bullets + 1);
            this.gunshot = true;
            this.recovered = false;
            this.lodgedOutcome = 0;
            this.bandaged = false;
            this.healingLeft = 12000;
        }

        public void cut() {
            this.cut = true;
            this.recovered = false;
            this.bandaged = false;
            this.healingLeft = Math.max(this.healingLeft, 2400);
        }

        public boolean canBandage() {
            return this.active() && !this.bandaged;
        }

        public boolean bandage() {
            if (!this.canBandage()) {
                return false;
            }
            if(this.infected&&!this.cut&&!this.gunshot&&!this.bitten){this.cut=true;this.healingLeft=Math.max(this.healingLeft,2400);}
            this.bandaged = true;
            this.dressingAge = 0;
            this.dirty = false;
            this.infected = false;
            if (this.bullets > 0 && this.lodgedOutcome == 0)
                this.lodgedOutcome = java.util.concurrent.ThreadLocalRandom.current().nextBoolean() ? 1 : 2;
            return true;
        }

        public void tick() {
            if (this.bandaged && ++this.dressingAge >= 2400) this.dirty = true;
            if (this.active() && this.bandaged && !this.infected && (this.bullets == 0 || this.lodgedOutcome == 1) && this.healingLeft > 0 && --this.healingLeft == 0) {
                this.gunshot = false;
                this.cut = false;
                this.bitten = false;
                this.recovered = true;
            }
        }

        public boolean blocksHealing() {
            return this.active() && (!this.bandaged || this.infected || (this.bullets > 0 && this.lodgedOutcome != 1));
        }
    }
}

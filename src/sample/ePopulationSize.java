package sample;

public enum ePopulationSize {
    small(50_000),
    medium(100_000),
    big(200_000);
    public int amountInThousand;
    ePopulationSize(int amountInnThousand)
    {
        this.amountInThousand = amountInnThousand;
    }
    public static ePopulationSize getMaxAmount(ePopulationSize eSize){
        switch (eSize) {
            case small: return ePopulationSize.small;
            case medium: return ePopulationSize.medium;
            case big: return ePopulationSize.big;
            default: return ePopulationSize.medium;
        }
    }
}

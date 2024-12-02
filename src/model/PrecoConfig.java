package model;

public class PrecoConfig {
    private static double precoSimples = 0.05;
    private static double precoEspecial = 0.08;
    private static double precoPremium = 0.12;

    public static double getPrecoSimples() {
        return precoSimples;
    }

    public static void setPrecoSimples(double preco) {
        precoSimples = preco;
    }

    public static double getPrecoEspecial() {
        return precoEspecial;
    }

    public static void setPrecoEspecial(double preco) {
        precoEspecial = preco;
    }

    public static double getPrecoPremium() {
        return precoPremium;
    }

    public static void setPrecoPremium(double preco) {
        precoPremium = preco;
    }
}

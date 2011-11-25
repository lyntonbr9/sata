package sata.metastock.util;

import java.math.BigDecimal;

import sata.domain.util.IConstants;

public class BlackScholes implements IConstants{

    /**
     * Prices European style options usign the Black-Scholes formula.
     * @param call true for call options, false for puts
     * @param precoAcao the price of the underlying
     * @param precoExercicioOpcao the strike of the option
     * @param tempoParaVencimentoOpcaoEmAnos the time to expiration, in years
     * @param taxaDeJuros the risk free interest rate
     * @param volatilidade the volatility
     * @return the Black-Scholes option price
     */
    public static double blackScholes(boolean call, double precoAcao, double precoExercicioOpcao, double tempoParaVencimentoOpcaoEmAnos, double taxaDeJuros, double volatilidade)
    {
        double d1, d2;

        double sqrtT = Math.sqrt(tempoParaVencimentoOpcaoEmAnos);
        double vsqrtT = volatilidade * sqrtT;
        d1 = (Math.log(precoAcao / precoExercicioOpcao) + (taxaDeJuros + volatilidade * volatilidade / 2) * tempoParaVencimentoOpcaoEmAnos) / vsqrtT;
        d2 = d1 - vsqrtT;

        if (call)
            return precoAcao * N(d1) - precoExercicioOpcao * Math.exp(-taxaDeJuros * tempoParaVencimentoOpcaoEmAnos) * N(d2);
        else
            return precoExercicioOpcao * Math.exp(-taxaDeJuros * tempoParaVencimentoOpcaoEmAnos) * N(-d2) - precoAcao * N(-d1);
    }

    /**
     * @param X the value
     * @return the cumulative normal distribution for the value
     */
    public static double N(double X)
    {
        double L, K, w;
        double a1 = 0.31938153, a2 = -0.356563782, a3 = 1.781477937, a4 = -1.821255978, a5 = 1.330274429;

        L = Math.abs(X);
        K = 1.0 / (1.0 + 0.2316419 * L);
        w = 1.0 - 1.0 / Math.sqrt(2.0 * Math.PI) * Math.exp(-L * L / 2) * (a1 * K + a2 * K * K + a3
                * Math.pow(K, 3) + a4 * Math.pow(K, 4) + a5 * Math.pow(K, 5));

        if (X < 0.0)
            w = 1.0 - w;
        return w;
    }


    public static void main(String[] args) {
    	
		double precoAcao = 21.66;   // preço da ação
        double precoExercicioOpcao = 19.66; 	// strike
        double qtdDiasFaltaEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_1_MES_VENC);	// tempo em anos
        double volatilidade = BlackScholes.getVolatilidade();
        
        System.out.println("Pctgem dias em anos: " + qtdDiasFaltaEmAnos);
        double valorOpcao = BlackScholes.blackScholes(false, precoAcao, precoExercicioOpcao, qtdDiasFaltaEmAnos, TAXA_DE_JUROS, volatilidade); 
        System.out.println("Valor da Opcao: " + valorOpcao);
        System.out.println("VI: " + getVI(false, precoAcao, precoExercicioOpcao));
        getVE(false, precoAcao, precoExercicioOpcao, valorOpcao);
        
    }
    
    public static double getVolatilidade()
    {
    	return 0.27; //27%
    }
    
    public static double getQtdDiasEmAnos(int qtdDiasFaltamParaVencimento)
    {
    	return new BigDecimal(qtdDiasFaltamParaVencimento).divide(new BigDecimal(QTD_DIAS_ANO), 4, BigDecimal.ROUND_HALF_UP).doubleValue(); //4 casas decimais
    }
    
    public static double getVI(boolean call, double precoAcao, double precoExercicioOpcao)
    {
    	if (call) //SE FOR CALL
    		if (precoAcao >= precoExercicioOpcao)
    			return precoAcao - precoExercicioOpcao;
    		else
    			return 0;
    	else //SE FOR PUT
    		if (precoAcao <= precoExercicioOpcao)
    			return precoExercicioOpcao - precoAcao;
    		else
    			return 0;
    }
    
    public static double getVE(boolean call, double precoAcao, double precoExercicioOpcao, double valorOpcao)
    {
    	double VI =0;
    	if (call) //SE FOR CALL
    		VI = getVI(true, precoAcao, precoExercicioOpcao);
    	else
    		VI = getVI(false, precoAcao, precoExercicioOpcao);
    	
    	//calcula o VE e a porcentagem em relacao a acao
    	double VE = valorOpcao - VI; 
    	double pctgemVE = (VE * 100)/precoAcao;
    	System.out.println("VE: " + VE);
        System.out.println("PctgemVE: " + pctgemVE + " %");
    	return VE;
    		
    }
    /**
     * 
     * @param precoAcima Se eh para calcular a opcao acima(true) ou abaixo(false)
     * @param precoAcao O preco da acao
     * @param ordem A ordem da acao Ex: 1º ITM, 2º ITM
     * @return
     */
    public static double getPrecoExercicio(boolean precoAcima, double precoAcao, double ordem)
    {
    	if (ordem == 0) //na ATM
    		return precoAcao;
    	if (precoAcima)
    		return precoAcao + (ordem * PCTGEM_OPCAO * precoAcao);
    	else
    		return precoAcao - (ordem * PCTGEM_OPCAO * precoAcao);
    }
    
    
    
}


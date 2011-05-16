package sata.domain.simulacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.metastock.robos.Cotacao;

public class SimulacaoAcaoAltaVarPoucoTempo implements ISimulacao{

	@Override
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		
		System.out.println(listaDasCotacoes.size());
		
		double[] valor = getDiasDeVariacao(listaDasCotacoes);
		ArrayList chaves = marcaDiasChave(getDiasDeVariacao(listaDasCotacoes));
		
		ArrayList dias = calculaNumeroDiasAteRetorno(chaves,listaDasCotacoes,valor);
		
	/*	for(int i=0;i<listaDasCotacoes.size();i++){
			
			//System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getCodigo());
			System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getFechamento());
		}
	*/	
		for(int i=0;i<chaves.size();i++){
			
			//System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getCodigo());
			CotacaoAtivoTO cotacao = listaDasCotacoes.get(((Integer)chaves.get(i)).intValue());
			System.out.println(cotacao.getPeriodo() + " " + ((Integer)dias.get(i)).toString().toString());
		}
		
		return null;
	}

	public static void main(String[] args) {
		SimulacaoAcaoAltaVarPoucoTempo s = new SimulacaoAcaoAltaVarPoucoTempo();
		DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		ICotacaoAtivoDAO caDAO = factory.getCotacaoAtivoDAO();
		List<CotacaoAtivoTO> listaDasCotacoes = caDAO.getCotacoesDoAtivo("USIM5", "2011");
		
		
		s.getResultado(listaDasCotacoes, null);
	}
	
	public double[] getDiasDeVariacao(List<CotacaoAtivoTO> listaDasCotacoes){
		
		
		double[] prc = new double[listaDasCotacoes.size()];
		
		for(int i=2;i<listaDasCotacoes.size();i++){
			BigDecimal cot1 = new BigDecimal(Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(i-2)).getFechamento()));
			BigDecimal cot2 = new BigDecimal(Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getFechamento()));
			cot2.multiply(new BigDecimal(-1));
			prc[i] = cot2.add(cot1.multiply(new BigDecimal(-1))).divide(cot1,BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
			//System.out.println(prc[i]);
			
		}
		
		return prc;
	}
	
	public ArrayList marcaDiasChave(double[] var){
		
		
		int alta = 0;
		int baixa = 0;
		ArrayList chaves = new ArrayList();
		
		for(int i=2;i<var.length;i++){
			if(var[i] >= 0.05){
				alta++;
				if(baixa>=2){
					
					chaves.add(new Integer(i-2));
				}
				baixa =0 ;
			}else if (var[i] <= -0.05){
				baixa++;
				if(alta>=2){
					
					chaves.add(new Integer(i-2));
				}
				alta=0;
			}
		}
		
		
		return chaves;
	}
	
	public ArrayList calculaNumeroDiasAteRetorno(ArrayList chaves, List<CotacaoAtivoTO> listaDasCotacoes,double[] valor){
		
		
		ArrayList nDias = new ArrayList();
		
		for(int i=0;i<chaves.size();i++){
		
			int contador = 0;
			
			int index = ((Integer)chaves.get(i)).intValue();
			double valorBase =Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(index)).getFechamento());
			double valorVariacao = valor[index+2];
			
			
			boolean subida =true;
			if(valorVariacao < 0){
				subida = false;
				
			}
			
			boolean fim =false;
			int j = 2;
			while(!fim){
				
				if(subida && (index + j) < listaDasCotacoes.size() && Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(index + j)).getFechamento())>=valorBase){
					contador++;
					j++;
				}else if(subida){					
						fim = true;
					
				}
				if(!subida && (index + j) < listaDasCotacoes.size() && Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(index + j)).getFechamento())<=valorBase){
					contador++;
					j++;
				}else if(!subida){					
						fim = true;
					
				}
				
				
			}
			nDias.add(new Integer(contador));
		}
		
		return nDias;
	}
	
	

}

package sata.domain.dao.arquivo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.to.CotacaoAtivoTO;

public class ArquivoCotacaoAtivoDAO implements ICotacaoAtivoDAO {

	private FileInputStream fisArqListaCotacaoDeAtivos;
	
	public void setArquivoListaCotacaoDeAtivos(String pathArqListaCotacaoDeAtivos){
		try {
			fisArqListaCotacaoDeAtivos = new FileInputStream(pathArqListaCotacaoDeAtivos);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//TODO: Fazer esta porra
	public CotacaoAtivoTO getCotacaoAtivo(String codigo){
		CotacaoAtivoTO caTO = new CotacaoAtivoTO();
		return caTO;
	}
	
	public List<CotacaoAtivoTO> getCotacoesDosAtivos(){
		List<CotacaoAtivoTO> listaDeCotacesDeAtivos = new ArrayList<CotacaoAtivoTO>();
		if (this.fisArqListaCotacaoDeAtivos != null)
		{
			DataInputStream disEntrada = new DataInputStream(this.fisArqListaCotacaoDeAtivos);
			BufferedReader brEntrada = new BufferedReader(new InputStreamReader(disEntrada));
			try {
				String conteudoLinha = "";
				while((conteudoLinha = brEntrada.readLine()) != null){
					String[] cotacaoDoAtivo = conteudoLinha.split(" ");
					CotacaoAtivoTO caTO = new CotacaoAtivoTO();
					caTO.setCodigo(cotacaoDoAtivo[0]);
					caTO.setAbertura(cotacaoDoAtivo[1]);
					caTO.setMaxima(cotacaoDoAtivo[2]);
					caTO.setMinima(cotacaoDoAtivo[3]);
					caTO.setFechamento(cotacaoDoAtivo[4]);
					caTO.setPeriodo("D"); //cotacao diaria
					listaDeCotacesDeAtivos.add(caTO);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					brEntrada.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return listaDeCotacesDeAtivos;
	}
}

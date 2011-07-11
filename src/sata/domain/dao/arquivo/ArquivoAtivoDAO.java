package sata.domain.dao.arquivo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.IAtivoDAO;
import sata.domain.to.AtivoTO;

@Deprecated
//public class ArquivoAtivoDAO implements IAtivoDAO{
public class ArquivoAtivoDAO{
	
	private FileInputStream fisArqListaDeAtivos;
	
	public void setArquivoListaDeAtivos(String pathArqListaDeAtivos){
		try {
			fisArqListaDeAtivos = new FileInputStream(pathArqListaDeAtivos);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public AtivoTO getAtivo(String codigo){
		return new AtivoTO(codigo);
	}
	
	public List<String> getCodigosAtivos(){
		List<String> listaDeAtivos = new ArrayList<String>();
		if (this.fisArqListaDeAtivos != null)
		{
			DataInputStream disEntrada = new DataInputStream(this.fisArqListaDeAtivos);
			BufferedReader brEntrada = new BufferedReader(new InputStreamReader(disEntrada));
			try {
				String conteudoLinha = "";
				while((conteudoLinha = brEntrada.readLine()) != null){
					listaDeAtivos.add(conteudoLinha.trim());
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
		return listaDeAtivos;
	}

}

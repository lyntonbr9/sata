package sata.domain.alert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.domain.dao.IAcompanhamentoDAO;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.AcompOpcaoTO;
import sata.domain.to.AcompanhamentoTO;
import sata.domain.to.OpcaoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class AcompOpcoes implements IConstants {
	
	public static void verificarOpcoes() throws Exception {
		IAcompanhamentoDAO acompDAO = SATAFactoryFacade.getAcompanhamentoDAO();
		IOpcaoDAO opcaoDAO = SATAFactoryFacade.getOpcaoDAO();
		for (AcompanhamentoTO acompanhamento : acompDAO.listar()) {
			BigDecimal precoAcaoAtual = acompanhamento.getPrecoAcaoAtual();
			String msg = "Acompanhamento " + acompanhamento.getNome() + "\n";
			msg += "- Preco Acao = "   + SATAUtil.formataNumero(precoAcaoAtual) + "\n";
			msg += "- Volatilidade = {*}% \n";
			int noPreco = 0;
			List<OpcaoTO> opcoes = opcaoDAO.pesquisa(acompanhamento.getAcao(), acompanhamento.getDataVencimento());
			for (OpcaoTO opcao : opcoes) {
				BigDecimal precoExercicio = opcao.getPrecoExercicio();
				BigDecimal percentual = precoExercicio.subtract(precoAcaoAtual).divide(precoAcaoAtual, RoundingMode.HALF_EVEN).multiply(CEM);
				for (AcompOpcaoTO acompOpcao : acompanhamento.getAcompanhamentos()) {
					if (percentual.doubleValue() >= acompOpcao.getPercToleradoInferior()
							&& percentual.doubleValue() <= acompOpcao.getPercToleradoSuperior()) {
//						if (true) {
						msg += "- Opcao " + acompOpcao.getPercExercicio() + "%: "
						+ opcao.getCodigo() 
						+ "(" + SATAUtil.formataNumero(precoExercicio) + ")"
						+ " => " + SATAUtil.formataNumero(percentual) + "%"
						+ " = " + SATAUtil.formataNumero(opcao.getPrecoAtual())
						+ " (B&S = {["+ opcao.getCodigo() + "]})\n";
						noPreco++;
					}
				}
			}
			if (noPreco > 0) {
				msg = atualizaMsg(opcoes, acompanhamento, msg);
				if (noPreco >= acompanhamento.getAcompanhamentos().size()) {
					String assunto = SATAUtil.getMessage(MSG_ACOMPANHAMENTO_EMAIL_ASSUNTO, acompanhamento.getNome());
					SATAUtil.sendMail(assunto, msg, acompanhamento.getInvestidor().getEmail());
				}
			}
			else {
				msg += "Nenhuma opção se enquadra...";
			}
			System.out.println("---");
			System.out.println(msg);
		}
	}
	
	private static BigDecimal calculaVolatilidade(List<OpcaoTO> opcoes, AcompanhamentoTO acompanhamento) {
		OpcaoTO opcaoMaisATM = getOpcaoMaisATM(opcoes, acompanhamento);
		BigDecimal precoAcao = acompanhamento.getPrecoAcaoAtual();
		BigDecimal precoOpcao = opcaoMaisATM.getPrecoAtual();
		BigDecimal precoExercicioOpcao = opcaoMaisATM.getPrecoExercicio();
		int diasParaVencimento = SATAUtil.getDiferencaDias(new Date(), opcaoMaisATM.getDataVencimento());
		BigDecimal taxaJuros = new BigDecimal(SATAUtil.getTaxaDeJuros());
		return PrecoOpcao.calculaVolatilidade(true, precoAcao, precoExercicioOpcao, diasParaVencimento, precoOpcao, taxaJuros);
	}
	
	private static BigDecimal getBlackScholes(OpcaoTO opcao, AcompanhamentoTO acompanhamento, BigDecimal volatilidade) {
		BigDecimal precoAcao = acompanhamento.getPrecoAcaoAtual();
		BigDecimal precoExercicioOpcao = opcao.getPrecoExercicio();
		int diasParaVencimento = SATAUtil.getDiferencaDias(new Date(), opcao.getDataVencimento());
		BigDecimal taxaJuros = new BigDecimal(SATAUtil.getTaxaDeJuros());
		return PrecoOpcao.blackScholes(true, precoAcao, precoExercicioOpcao, diasParaVencimento, volatilidade, taxaJuros);
	}
	
	private static OpcaoTO getOpcaoMaisATM(List<OpcaoTO> opcoes, AcompanhamentoTO acompanhamento) {
		double menorDiferenca = Double.POSITIVE_INFINITY;
		OpcaoTO opcaoMaisATM = null;
		for (OpcaoTO opcao : opcoes) {
			double diferenca = opcao.getPrecoExercicio().subtract(acompanhamento.getPrecoAcaoAtual()).abs().doubleValue();
			if (diferenca < menorDiferenca) {
				menorDiferenca = diferenca;
				opcaoMaisATM = opcao;
			}
		}
		return opcaoMaisATM;
	}
	
	private static String atualizaMsg(List<OpcaoTO> opcoes, AcompanhamentoTO acompanhamento, String msg) {
		BigDecimal volatilidade = calculaVolatilidade(opcoes, acompanhamento);
		msg = msg.replace("{*}",SATAUtil.formataNumero(volatilidade.multiply(CEM)));
		for (OpcaoTO opcao : opcoes) {
			msg = msg.replace("{["+ opcao.getCodigo() + "]}", 
					SATAUtil.formataNumero(getBlackScholes(opcao, acompanhamento, volatilidade)));
		}
		return msg;
	}
	
	public static void main(String[] args) throws Exception {
		verificarOpcoes();
	}
}

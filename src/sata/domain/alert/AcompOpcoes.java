package sata.domain.alert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import javax.mail.MessagingException;

import sata.domain.dao.IAcompOpcoesDAO;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.AcompOpcaoTO;
import sata.domain.to.AcompanhamentoTO;
import sata.domain.to.OpcaoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;
import sata.metastock.mail.SendMailUsingAuthentication;

public class AcompOpcoes implements IConstants {
	
	private static Locale defaultLocale = LOCALE_BRASIL;
	
	public static void verificarOpcoes() throws Exception {
		IAcompOpcoesDAO acompDAO = SATAFactoryFacade.getAcompOpcoesDAO();
		IOpcaoDAO opcaoDAO = SATAFactoryFacade.getOpcaoDAO();
		for (AcompanhamentoTO acompanhamento : acompDAO.listar()) {
			BigDecimal precoAcaoAtual = acompanhamento.getPrecoAcaoAtual();
			String msg = "Acompanhamento " + acompanhamento.getNome() + "\n";
			int noPreco = 0;
			for (OpcaoTO opcao : opcaoDAO.pesquisa(acompanhamento.getAcao(), acompanhamento.getDataVencimento())) {
				BigDecimal precoExercicio = opcao.getPrecoExercicio();
				BigDecimal percentual = precoExercicio.subtract(precoAcaoAtual).divide(precoAcaoAtual, RoundingMode.HALF_EVEN).multiply(CEM);
				for (AcompOpcaoTO acompOpcao : acompanhamento.getAcompanhamentos()) {
					if (percentual.doubleValue() >= acompOpcao.getPercToleradoInferior()
							&& percentual.doubleValue() <= acompOpcao.getPercToleradoSuperior()) {
						msg += opcao.getCodigo() 
						+ ": Preco Exercicio = " + SATAUtil.formataNumero(precoExercicio, defaultLocale)
						+ " --> Preço Acao = "   + SATAUtil.formataNumero(precoAcaoAtual, defaultLocale)
						+ " --> " + SATAUtil.formataNumero(percentual, defaultLocale) + "%"
						+ " (" + acompOpcao.getPercExercicio() + " +/- " + acompOpcao.getPercTolerancia() + "%)\n";
						noPreco++;
					}
				}
			}
			if (noPreco >= acompanhamento.getAcompanhamentos().size()) {
				String assunto = SATAUtil.getMessage(MSG_ACOMPANHAMENTO_EMAIL_ASSUNTO, acompanhamento.getNome());
				sendMail(assunto, msg, acompanhamento.getInvestidor().getEmail());
			}
			else if (noPreco == 0) {
				msg += "Nenhuma opção se enquadra...";
			}
			System.out.println("---");
			System.out.println(msg);
		}
	}
	
	private static void sendMail(String assunto, String msg, String... remetentes) throws MessagingException {
		System.out.println("Enviando e-mail de alerta...");
		SendMailUsingAuthentication.sendMail(remetentes, assunto, msg);
		System.out.println("E-mail enviado com sucesso!");
	}
}

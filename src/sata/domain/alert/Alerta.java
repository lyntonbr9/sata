package sata.domain.alert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import sata.domain.dao.IAlertaDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.AlertaTO;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.to.SerieOperacoesTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;
import sata.metastock.mail.SendMailUsingAuthentication;

public class Alerta implements IConstants {
	
	private static Locale defaultLocale = LOCALE_BRASIL;

	public static void verificarAlertasOperacoesAtivos() throws SQLException, MessagingException {
		IAlertaDAO dao = SATAFactoryFacade.getAlertaDAO();
		List<AlertaTO> alertasAtivos = dao.listaAlertasAtivos();
		for (AlertaTO alerta: alertasAtivos) {
			for (SerieOperacoesTO serie: alerta.getSeries()) {
				BigDecimal valorAbertura = BigDecimal.ZERO;
				BigDecimal valorFechamento = BigDecimal.ZERO;
				String msg = "---\n";
				msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_ALERTA) + " " + alerta.getNome() + "\n";
				msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_SERIE) + " " 
					+ SATAUtil.getMessage(MSG_ALERTA_LABEL_DE) + " " 
					+ SATAUtil.formataData(serie.getDataExecucao()) + "\n";
				for (OperacaoRealizadaTO op: serie.getOperacoes()) {
					valorAbertura = valorAbertura.add(op.getValorReal().multiply(new BigDecimal(op.getQtdLotes())));
					BigDecimal valorAtualTotal = op.getValorAtual().multiply(new BigDecimal(op.getQtdLotes()));
					valorFechamento = valorFechamento.add(valorAtualTotal);
					msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_OPERACAO) + " " + op.getPosicao() 
						+ " " + op.getAtivo() + " = " 
						+ SATAUtil.formataNumero(op.getValorReal(),defaultLocale) + " --> " 
						+ SATAUtil.formataNumero(op.getValorAtual(),defaultLocale) + "\n";
				}
				BigDecimal valorInvestido = getValorInvestido(serie);
				BigDecimal valorSerie = valorAbertura.add(valorFechamento);
				BigDecimal percentual = valorSerie.divide(valorInvestido, RoundingMode.HALF_EVEN).multiply(CEM);
				msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_PRECO_ACAO) + ": " 
					+  SATAUtil.formataNumero(serie.getPrecoAcaoAtual(),defaultLocale) + "\n";
				msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_VALOR_INVESTIDO) + ": " 
					+ SATAUtil.formataNumero(valorInvestido,defaultLocale) + "\n";
				msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_VALOR_SERIE) + ": " 
					+ SATAUtil.formataNumero(valorSerie,defaultLocale) + "\n";
				msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_PREC_SERIE) + ": " 
					+ SATAUtil.formataNumero(percentual,defaultLocale)+ "%";
				
				if (alerta.alertaPorcentagemGanho(percentual)) {
					String assunto = SATAUtil.getMessage(MSG_ALERTA_EMAIL_ASSUNTO_GANHO,
							alerta.getPorcentagemGanho().toString(),SATAUtil.getSomenteDataAtualFormatada()); 
					sendMail(assunto, msg, serie.getInvestidor().getEmail());
				} else if (alerta.alertaPorcentagemPerda(percentual)) {
					String assunto = SATAUtil.getMessage(MSG_ALERTA_EMAIL_ASSUNTO_PERDA,
							alerta.getPorcentagemGanho().toString(),SATAUtil.getSomenteDataAtualFormatada());
					sendMail(assunto, msg, serie.getInvestidor().getEmail());
				}
				System.out.println(msg);
			}
		}
	}
	
	private static BigDecimal getValorInvestido(SerieOperacoesTO serie) {
		switch (serie.getAlerta().getTipoCalculoVI()) {
		case PRECO_ACAO:
			BigDecimal precoTotalAcao = serie.getPrecoAcao().multiply(new BigDecimal(serie.getQtdLotesAcao()));
			BigDecimal percentual = (new BigDecimal(serie.getAlerta().getPercCalculoVI())).divide(CEM);
			return precoTotalAcao.multiply(percentual);
			
		case CUSTO_MONTAGEM:
			BigDecimal valorAbertura = BigDecimal.ZERO;
			for (OperacaoRealizadaTO op: serie.getOperacoes()) 
				valorAbertura = valorAbertura.add(op.getValorReal().multiply(new BigDecimal(op.getQtdLotes())));
			return valorAbertura;

		default:
			return BigDecimal.ZERO;
		}
	}
	
	private static void sendMail(String assunto, String msg, String... remetentes) throws MessagingException {
		System.out.println("Enviando e-mail de alerta...");
		SendMailUsingAuthentication.sendMail(remetentes, assunto, msg);
		System.out.println("E-mail enviado com sucesso!");
	}
}

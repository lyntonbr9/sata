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
			BigDecimal valorAbertura = BigDecimal.ZERO;
			BigDecimal valorFechamento = BigDecimal.ZERO;
			for (SerieOperacoesTO serie: alerta.getSeries()) {
				String msg = "Alerta " + alerta.getNome() + "\n";
				msg += "Série de " + SATAUtil.formataData(serie.getDataExecucao()) + "\n";
				for (OperacaoRealizadaTO op: serie.getOperacoes()) {
					valorAbertura = valorAbertura.add(op.getValor().multiply(new BigDecimal(op.getQtdLotes())));
					BigDecimal valorAtualTotal = op.getValorAtual().multiply(new BigDecimal(op.getQtdLotes()));
					valorFechamento = valorFechamento.add(valorAtualTotal);
					msg += "Operação " + op.getPosicao() + " " + op.getAtivo() + " = " 
							+ SATAUtil.formataNumero(op.getValor(),defaultLocale) + " --> " 
							+ SATAUtil.formataNumero(op.getValorAtual(),defaultLocale) + "\n";
				}
				BigDecimal valorInvestido = getValorInvestido(serie);
				BigDecimal valorSerie = valorAbertura.add(valorFechamento);
				BigDecimal percentual = valorSerie.divide(valorInvestido, RoundingMode.HALF_EVEN).multiply(CEM);
				msg += "Valor Investido: " + SATAUtil.formataNumero(valorInvestido,defaultLocale) + "\n";
				msg += "Valor Série: " + SATAUtil.formataNumero(valorSerie,defaultLocale) + "\n";
				msg += "Percentual Série: " + SATAUtil.formataNumero(percentual,defaultLocale)+ "%";
				
				if (alerta.alertaPorcentagemGanho(percentual)) {
					String assunto = "[SATA-PROJ] Alerta de Ganho Percentual >= " + 
						alerta.getPorcentagemGanho() + "% em " + SATAUtil.getSomenteDataAtualFormatada(); 
					sendMail(assunto, msg, serie.getInvestidor().getEmail());
				} else if (alerta.alertaPorcentagemPerda(percentual)) {
					String assunto = "[SATA-PROJ] Alerta de Perda Percentual <= " + 
					alerta.getPorcentagemPerda() + "% em " + SATAUtil.getSomenteDataAtualFormatada();
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

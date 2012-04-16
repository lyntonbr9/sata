package sata.auto.gui.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sata.domain.alert.AcompOpcoes;
import sata.domain.alert.AlertaOperacao;
import sata.domain.dao.hibernate.HibernateUtil;
import sata.domain.util.SATAUtil;
import sata.metastock.robos.InfoMoney;

/**
 * Servlet implementation class RobotServlet
 */
public class RobotServlet extends HttpServlet {
	
	private static final long serialVersionUID = -2956783815151391222L;

	private void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("--- Inicio da execucao do Robo em " + SATAUtil.getDataAtualFormatada());
			if (isBolsaAberta()) {
				System.out.println("Bolsa aberta!");
				AlertaOperacao.verificarAlertasOperacoesAtivos();
				AcompOpcoes.verificarOpcoes();
			}
			else System.out.println("Bolsa fechada!");
			HibernateUtil.closeCurrentSession();
			System.out.println("--- Fim da execucao do Robo em " + SATAUtil.getDataAtualFormatada());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isBolsaAberta() {
		return InfoMoney.isBolsaAberta();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}
}

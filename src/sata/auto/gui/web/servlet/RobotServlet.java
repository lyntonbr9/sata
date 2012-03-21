package sata.auto.gui.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sata.domain.alert.OperacaoLhamaOpcoes;
import sata.domain.util.SATAUtil;

/**
 * Servlet implementation class RobotServlet
 */
public class RobotServlet extends HttpServlet {
	
	private static final long serialVersionUID = -2956783815151391222L;

	private void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = "Robot running at " + SATAUtil.getDataAtualFormatada();
		System.out.println(msg);
		OperacaoLhamaOpcoes.alertaLhama();
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

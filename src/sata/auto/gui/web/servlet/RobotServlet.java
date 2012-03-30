package sata.auto.gui.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sata.domain.alert.Alerta;
import sata.domain.util.SATAUtil;

/**
 * Servlet implementation class RobotServlet
 */
public class RobotServlet extends HttpServlet {
	
	private static final long serialVersionUID = -2956783815151391222L;

	private void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- Start of Robot running at " + SATAUtil.getDataAtualFormatada());
		try {
			Alerta.verificarAlertasOperacoesAtivos();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--- End of Robot running at " + SATAUtil.getDataAtualFormatada());
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

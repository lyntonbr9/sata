package sata.auto.gui.web.mbean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;

import sata.auto.web.util.FacesUtil;
import sata.domain.dao.IInvestidorDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.InvestidorTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

@ManagedBean
@SessionScoped
public class InvestidorMB implements IConstants {
	
	public InvestidorMB() {
		try {
			investidores = SATAFactoryFacade.getInvestidorDAO().listar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private InvestidorTO investidor = new InvestidorTO();
	private List<InvestidorTO> investidores = new ArrayList<InvestidorTO>();
	private String pageAfterLogin;
	private int tentativas;
	private boolean logado = false;
	
	public void login() {
		try {
			logado = isOk();
			if (logado) FacesUtil.redirect(pageAfterLogin);
			else tentativas++;
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void logout() {
		try {
			tentativas = 0;
			investidor = new InvestidorTO();
			logado = false;
			FacesUtil.redirect(FacesUtil.getPaginaAtual());
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	private boolean isOk() throws SQLException {
		boolean ok = true;
		if (StringUtils.isEmpty(investidor.getEmail())) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_LOGIN_LABEL_EMAIL);
			ok = false;
		}
		if (StringUtils.isEmpty(investidor.getSenha())) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_LOGIN_LABEL_SENHA);
			ok = false;
		}
		
		if (ok) {
			investidor.setSenha(SATAUtil.encrypt(investidor.getSenha()));
			IInvestidorDAO dao = SATAFactoryFacade.getInvestidorDAO();
			investidor = dao.recuperar(investidor.getEmail());
			if (investidor == null) {
				FacesUtil.addError(MSG_ERRO_ERRO_EMAIL_NAO_CADASTRADO);
				ok = false;
			}
			else if (!investidor.getSenha().equals(investidor.getSenha())) {
				FacesUtil.addError(MSG_ERRO_ERRO_SENHA_INCORRETA);
				ok = false;
			}
		}
		return ok;
	}

	public InvestidorTO getInvestidor() {
		return investidor;
	}
	public void setInvestidor(InvestidorTO investidor) {
		this.investidor = investidor;
	}
	public String getPageAfterLogin() {
		return pageAfterLogin;
	}
	public void setPageAfterLogin(String pageAfterLogin) {
		this.pageAfterLogin = pageAfterLogin;
	}
	public boolean isLogado() {
		return logado;
	}
	public void setLogado(boolean logado) {
		this.logado = logado;
	}
	public int getTentativas() {
		return tentativas;
	}
	public void setTentativas(int tentativas) {
		this.tentativas = tentativas;
	}
	public List<InvestidorTO> getInvestidores() {
		return investidores;
	}
	public void setInvestidores(List<InvestidorTO> investidores) {
		this.investidores = investidores;
	}
	
	public static InvestidorTO getInvestidorLogado() {
		return FacesUtil.getMB(InvestidorMB.class).getInvestidor();
	}

}

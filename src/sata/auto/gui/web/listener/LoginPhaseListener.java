package sata.auto.gui.web.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import sata.auto.gui.web.mbean.InvestidorMB;
import sata.domain.util.FacesUtil;

public class LoginPhaseListener implements PhaseListener {
	private static final long serialVersionUID = -6002715477049826372L;
	

	@Override
	public void afterPhase(PhaseEvent event) {
		InvestidorMB investidorMB = FacesUtil.getMB(InvestidorMB.class);
		boolean logado = true;
		if (investidorMB == null) logado = false;
		else if (!FacesUtil.getPaginaAtual().contains("login"))
			if (!investidorMB.isLogado())
				logado = false;
		if (!logado) {
			if (investidorMB != null)
				investidorMB.setPageAfterLogin(FacesUtil.getPaginaAtual());
			FacesUtil.navigateTo("login");
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}

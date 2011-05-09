/*
 * Created on 12/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.bovespa;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.metastock.data.Merge2;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Menu extends JPanel{
	
	
//	JTextField textfield = new JTextField("");
	JTextField diasAtras = new JTextField("");
	
	JTextField primeiro = new JTextField("");
	JTextField ultimo = new JTextField("");
	
   	JTextField dia = new JTextField("",2);
   	JTextField mes = new JTextField("",2);

   	JTextField ano = new JTextField("",4);
   	
   	JTextField txtOpacidade = new JTextField("",4);
   	JComboBox cmbAcoes;

	private int cont = 0;
	
	private String[] acoesNome = {
	"ACES4 ACESITA PN 30.452.886 0,221",  
	"GETI4 AES TIETE PN * 31.055.306.159 0,332", 
	"ALLL11 ALL AMER LAT UNT 310.608.900 1,039",  
	"AMBV3 AMBEV ON * 3.801.992.141 0,616",  
	"AMBV4 AMBEV PN * 17.687.123.227 3,233",  
	"ARCZ6 ARACRUZ PNB 449.790.741 0,945",  
	"ARCE3 ARCELOR BR ON 216.591.586 1,508",  
	"BBDC3 BRADESCO ON 176.347.094 2,275",  
	"BBDC4 BRADESCO PN 463.832.994 6,376", 
	"BRAP4 BRADESPAR PN 55.452.310 0,750",  
	"BBAS3 BRASIL ON 122.519.000 1,113",  
	"BRTP3 BRASIL T PAR ON * 50.798.951.328 0,236",  
	"BRTP4 BRASIL T PAR PN * 216.958.303.310 0,527",  
	"BRTO4 BRASIL TELEC PN * 164.880.490.354 0,249",  
	"BRKM5 BRASKEM PNA 171.515.497 0,474",  
	"CCRO3 CCR RODOVIAS ON 114.785.167 0,465",  
	"CLSC6 CELESC PNB 416.579.779 0,121",  
	"CMIG3 CEMIG ON * 11.383.415.427 0,159",  
	"CMIG4 CEMIG PN * 88.818.386.310 1,469",  
	"CESP6 CESP PNB* 143.933.968.933 0,499",  
	"CGAS5 COMGAS PNA* 2.226.284.255 0,125",  
	"CNFB4 CONFAB PN 211.275.597 0,173",  
	"CTAX3 CONTAX ON 58.869.720 0,037",  
	"CTAX4 CONTAX PN 252.355.848 0,092",  
	"CPLE6 COPEL PNB* 100.938.573.772 0,449",  
	"CPSL3 COPESUL ON 61.707.013 0,337",  
	"CSAN3 COSAN ON 82.963.335 0,560",  
	"CTNM4 COTEMINAS PN * 3.589.300.491 0,123",  
	"CPFE3 CPFL ENERGIA ON 85.139.136 0,448",  
	"CYRE3 CYRELA REALT ON 99.510.943 0,731",  
	"DASA3 DASA ON 35.456.228 0,276",  
	"DURA4 DURATEX PN 65.013.924 0,298",  
	"ELET3 ELETROBRAS ON * 98.493.872.064 0,841",  
	"ELET6 ELETROBRAS PNB* 94.568.526.275 0,740",  
	"ELPL5 ELETROPAULO PNA* 7.496.975.347 0,130",  
	"EMBR3 EMBRAER ON 481.293.954 1,986",  
	"EBTP4 EMBRATEL PAR PN * 259.013.829.057 0,329",  
	"ENBR3 ENERGIAS BR ON 62.100.668 0,318",  
	"ETER3 ETERNIT ON 32.944.540 0,060",  
	"FFTL4 FOSFERTIL PN 22.989.527 0,097",  
	"GGBR3 GERDAU ON 48.582.363 0,227",  
	"GGBR4 GERDAU PN 292.711.829 1,644",  
	"GOAU4 GERDAU MET PN 118.929.990 0,822",  
	"GOLL4 GOL PN 51.912.331 0,781",  
	"GRND3 GRENDENE ON 19.881.903 0,064",  
	"GUAR3 GUARARAPES ON 4.629.267 0,073",  
	"IDNT3 IDEIASNET ON 56.033.627 0,033",  
	"MYPK4 IOCHP-MAXION PN 25.056.858 0,083",  
	"PTIP4 IPIRANGA PET PN 66.170.182 0,216",  
	"ITAU4 ITAUBANCO PN 561.101.993 7,072",  
	"ITSA4 ITAUSA PN 1.636.710.576 2,856",  
	"KLBN4 KLABIN S/A PN 478.255.993 0,408",  
	"LIGT3 LIGHT S/A ON * 14.241.519.361 0,045",  
	"RENT3 LOCALIZA ON 33.607.544 0,298",  
	"LAME4 LOJAS AMERIC PN * 26.677.110.698 0,457",  
	"LREN3 LOJAS RENNER ON 24.284.384 0,604",  
	"POMO4 MARCOPOLO PN 131.273.010 0,106",  
	"NATU3 NATURA ON 108.526.822 0,569",  
	"NETC4 NET PN 131.038.941 0,507",  
	"BNCA3 NOSSA CAIXA ON 30.772.784 0,269",  
	"OHLB3 OHL BRASIL ON 27.555.562 0,127",  
	"PCAR4 P.ACUCAR-CBD PN * 45.771.740.228 0,513",  
	"PMAM4 PARANAPANEMA PN 17.249.045 0,067",  
	"PRGA3 PERDIGAO S/A ON 69.574.455 0,315",  
	"PETR3 PETROBRAS ON 1.069.169.280 8,623",  
	"PETR4 PETROBRAS PN 1.559.270.000 11,532",  
	"PSSA3 PORTO SEGURO ON 28.627.495 0,249",  
	"RAPT4 RANDON PART PN 84.669.159 0,118",  
	"RSID3 ROSSI RESID ON 44.624.282 0,200",  
	"SBSP3 SABESP ON * 14.165.307.474 0,646",  
	"SDIA4 SADIA S/A PN 381.437.496 0,435",  
	"SLED4 SARAIVA LIVR PN 18.474.951 0,073",  
	"CSNA3 SID NACIONAL ON 140.401.959 1,630",  
	"CRUZ3 SOUZA CRUZ ON 75.613.722 0,499",  
	"SUBA3 SUBMARINO ON 51.458.770 0,431",  
	"SUZB5 SUZANO PAPEL PNA 111.907.135 0,301", 
	"SZPQ4 SUZANO PETR PN 53.648.921 0,037",  
	"TAMM4 TAM S/A PN 68.241.998 0,930",  
	"TNLP3 TELEMAR ON 58.869.713 0,691",  
	"TNLP4 TELEMAR PN 254.747.800 1,387",  
	"TMAR5 TELEMAR N L PNA 39.190.701 0,329",  
	"TMCP4 TELEMIG PART PN * 215.999.076.275 0,163",  
	"TLPP3 TELESP ON 24.356.873 0,194",  
	"TLPP4 TELESP PN 36.667.552 0,336",  
	"TCSL3 TIM PART S/A ON * 148.806.027.477 0,241",  
	"TCSL4 TIM PART S/A PN * 555.912.485.632 0,672",  
	"TBLE3 TRACTEBEL ON 204.111.730 0,698",  
	"TRPL4 TRAN PAULIST PN * 86.726.372.193 0,353",  
	"UGPA4 ULTRAPAR PN 30.631.686 0,226",  
	"UBBR11 UNIBANCO UNT 1.063.911.188 3,230",  
	"UNIP6 UNIPAR PNB 402.318.756 0,127",  
	"UOLL4 UOL PN 46.765.607 0,123",  
	"USIM3 USIMINAS ON 51.988.417 0,735",  
	"USIM5 USIMINAS PNA 106.620.295 1,318",  
	"VCPA4 V C P PN 97.358.515 0,624",  
	"VALE3 VALE R DOCE ON 687.313.572 5,828",  
	"VALE5 VALE R DOCE PNA 958.454.184 7,060",  
	"VIVO3 VIVO ON 55.939.189 0,113",  
	"VIVO4 VIVO PN 476.709.448 0,600",  
	"WEGE4 WEG PN 188.907.220 0,340"};  

	private String acoes[] = null;
	
    public Menu() {
    	
    	/*    	
		File arq = new File("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa\\");
		
		//BLOCO 1 - TODAS AS AÇÕES
		String[] todas = arq.list();
		ArrayList nomes5 = new ArrayList();
		for(int i=0;i<todas.length;i++){
			if(todas[i].replaceAll(".txt","").length()==5){
				nomes5.add(todas[i].replaceAll(".txt",""));
			}
		}
        nomes5.add("ALLL11");
		
        acoes = new String[nomes5.size()];
		for(int i=0;i<nomes5.size();i++){
			
			acoes[i] = (String)nomes5.get(i);
		}*/
		
		//IBrX
		
//		acoes = new String[acoesNome.length];
//		for(int i=0;i<acoesNome.length;i++){
//			acoes[i] = acoesNome[i].substring(0,acoesNome[i].indexOf(" "));
//		}
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		IAtivoDAO ativoDAO = daoFactory.getAtivoDAO();
		List<String> listaAtivos = ativoDAO.getCodigosAtivos();
		acoes = new String[listaAtivos.size()];
		for(int i=0; i < listaAtivos.size(); i++)
			acoes[i] = listaAtivos.get(i);
		
		cmbAcoes = new JComboBox(acoes);
		
		JButton next = new JButton("Próxima");
        next.addActionListener(new NextListener());
        
        JButton proximoDia = new JButton("Próximo dia");
        proximoDia.addActionListener(new IntervaloListener());
        
        JButton relatorio = new JButton("Relatorio 1");
        relatorio.addActionListener(new Relatorio1Listener());
        
        JButton compra = new JButton("Compra");
        compra.addActionListener(new CompraListener());
        
        JButton vende = new JButton("Vende");
        vende.addActionListener(new VendeListener());
        
        JButton verifica = new JButton("Verifica Percentual");
        verifica.addActionListener(new MomentoPrcListener());
        
        JRadioButton bDiario = new JRadioButton();
        bDiario.setSelected(true);
        bDiario.addActionListener(new DiaCandleListener());
        JRadioButton bSemanal = new JRadioButton();
        bSemanal.addActionListener(new SemanaCandleListener());
        JRadioButton bMensal = new JRadioButton();
        bMensal.addActionListener(new MesCandleListener());
        JLabel labelDiario = new JLabel("D");
        JLabel labelSemanal = new JLabel("S");
        JLabel labelMensal = new JLabel("M");
        JLabel lblOpacidade = new JLabel("Opacidade: ");
        
        // Associate the two buttons with a button group
        ButtonGroup group = new ButtonGroup();
        group.add(bDiario);
        group.add(bSemanal);
        group.add(bMensal);

        
        // Create a text field with some initial text and a default number of columns.
        // The number of columns controls the preferred width of the component;
        // each column is rougly the size of an M in the current font.
//        int cols = 6;
//        textfield = new JTextField(acoes[0], cols);
        diasAtras = new JTextField("", 3);
        diasAtras.addActionListener(new DiasListener());
        
        primeiro = new JTextField("0", 3);
        ultimo = new JTextField("120", 3);
        
        JButton atualiza = new JButton("Atualiza");
        atualiza.addActionListener(new AtualizaListener());
       
//        textfield.addActionListener(new MyActionListener());
        cmbAcoes.addActionListener(new ComboSelecionaAcoesListener());
        txtOpacidade.addActionListener(new OpacidadeListener());
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
//    	this.add(textfield);
    	this.add(cmbAcoes);
        this.add(next);
    	this.add(diasAtras);
    	this.add(primeiro);
    	this.add(ultimo);
    	this.add(proximoDia);
//    	this.add(relatorio);
//    	this.add(compra);
//    	this.add(vende);
    	this.add(verifica);
    	this.add(labelDiario);
    	this.add(bDiario);
    	this.add(labelSemanal);
    	this.add(bSemanal);
    	this.add(labelMensal);
    	this.add(bMensal);
    	this.add(atualiza);
    	this.add(lblOpacidade);
    	this.add(txtOpacidade);

        setPreferredSize(new Dimension(200, 50));
        setBackground(new Color(210,210,210));
    }//end constructor
    

    public String getAcao(){
    	//return textfield.getText();
    	return (String) cmbAcoes.getSelectedItem();
    }

    class OpacidadeListener implements ActionListener{
    	public void actionPerformed(ActionEvent evt){
    		JTextField txtSource = (JTextField)evt.getSource();
    		String valorOpacidade = txtSource.getText();
    		if (valorOpacidade.equalsIgnoreCase("") == false){
    			valorOpacidade = valorOpacidade.replace(",", ".");
    			if(Float.parseFloat(valorOpacidade) > 1)
    				valorOpacidade = "1.0";
    			MainFrame.setOpacidade(Float.parseFloat(valorOpacidade));
    		}
    			
    	}
    }
    
    class ComboSelecionaAcoesListener implements ActionListener{
    	public void actionPerformed(ActionEvent evt){
    		JComboBox cmbSource = (JComboBox)evt.getSource();
    		String acao = (String) cmbSource.getSelectedItem();
    		MainFrame.setAcao(acao);   			
    	}
    }
    
    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JTextField textfield = (JTextField)evt.getSource();
            MainFrame.setAcao(textfield.getText());
        }

	
    }
    
    class DiasListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JTextField diasAtras = (JTextField)evt.getSource();
            MainFrame.setDias(diasAtras.getText());
        }

	
    }
    
    class IntervaloListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	primeiro.setText("" + (Integer.parseInt(primeiro.getText()) + 1));
        	ultimo.setText("" + (Integer.parseInt(ultimo.getText()) + 1));
            MainFrame.setIntervaloDia(primeiro.getText(),ultimo.getText());
        }	
    }

    class NextListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           String acao = (String) cmbAcoes.getSelectedItem();
           
           for(int i=0;i<acoes.length;i++){
           	   if(acoes[i].equalsIgnoreCase(acao)){
           	   	
           	   		if(i==acoes.length-1){
	           	   		MainFrame.setAcao(acoes[0]);     
	           	   		cmbAcoes.setSelectedItem(acoes[0]);
	           	   		return;
           	   		}else{
	           	   		MainFrame.setAcao(acoes[i+1]);
	           	   		cmbAcoes.setSelectedItem(acoes[i+1]);
	           	   		return;
           	   		}	
           	   }
           }
        }
    }

//    class NextListener implements ActionListener {
//        public void actionPerformed(ActionEvent evt) {
//           String acao = textfield.getText();
//           
//           for(int i=0;i<acoes.length;i++){
//           	   if(acoes[i].equalsIgnoreCase(acao)){
//           	   	
//           	   		if(i==acoes.length-1){
//	           	   		MainFrame.setAcao(acoes[0]);     
//	           	   		textfield.setText(acoes[0]);
//	           	   		return;
//           	   		}else{
//	           	   		MainFrame.setAcao(acoes[i+1]);     
//	           	   		textfield.setText(acoes[i+1]);
//	           	   		return;
//           	   		}
//           	   	
//           	   		
//           	   }
//           }
//            
//        }
//    }
    
    class Relatorio1Listener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	
           MainFrame.setWaitCursor(true);	
           String relatorioStr = Calculo.getRelatorio1(acoes);
           
           
           JDialog rel = new JDialog();
           rel.setSize(700,400);
           JTextArea text = new JTextArea();
           text.setText(relatorioStr);
           JScrollPane scroll = new JScrollPane(text);
          
           
           
           rel.getContentPane().add(scroll);
		   
           
           rel.setVisible(true);
           MainFrame.setWaitCursor(false);	
            
        }
    }
	
    class AtualizaListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
                  
           JDialog data = new JDialog();
           data.setTitle("Digite o dia, o mês e o ano respectivamente:");
           data.setSize(400,100);
           
            JButton ok = new JButton("OK");
            ok.addActionListener(new AtualizaOkListener());
          
           
            data.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
           	data.getContentPane().add(dia);
           	data.getContentPane().add(mes);
           	data.getContentPane().add(ano);
           	data.getContentPane().add(ok);
    		data.setVisible(true);  
        }
    }
    
    class AtualizaOkListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
                  
        	try{
	            MainFrame.setWaitCursor(true);	
	            Merge2.merge(Integer.parseInt(ano.getText()),Integer.parseInt(mes.getText()),Integer.parseInt(dia.getText()));
	            JOptionPane.showMessageDialog(null,"Dados teoricamente atualizados");
	            
        	}catch(Exception e){
        		JOptionPane.showMessageDialog(null,"Erro");
        		JOptionPane.showMessageDialog(null,e.getMessage());
        	}finally{
        		MainFrame.setWaitCursor(false);	
        	
        	}
        }
    }
    
    class CompraListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {

        	MainFrame.compra();            
        }
    }
    
    class VendeListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.vende();
 
        }
    }
    
    class MomentoPrcListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.exibePercentualMomento();
 
        }
    }
    
    class DiaCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.setDiasCandle(1);
 
        }
    }
    
    class SemanaCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.setDiasCandle(5);
 
        }	
    }
    class MesCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.setDiasCandle(22);
 
        }
    }
}

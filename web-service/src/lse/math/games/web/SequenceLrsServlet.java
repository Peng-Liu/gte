package lse.math.games.web;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lse.math.games.Rational;
import lse.math.games.io.ColumnTextWriter;
import lse.math.games.io.ExtensiveFormXMLReader;
import lse.math.games.lcp.LCP;
import lse.math.games.lcp.LemkeAlgorithm;
import lse.math.games.lcp.LemkeAlgorithm.LemkeException;
//import lse.math.games.lcp.LemkeAlgorithm.LemkeInitException;
import lse.math.games.lcp.LemkeAlgorithm.RayTerminationException;
import lse.math.games.matrix.BimatrixSolver;
import lse.math.games.matrix.Equilibria;
import lse.math.games.matrix.Equilibrium;
import lse.math.games.tree.ExtensiveForm;
import lse.math.games.tree.Move;
import lse.math.games.tree.Player;
import lse.math.games.tree.SequenceForm;
import lse.math.games.tree.SequenceForm.ImperfectRecallException;
import lse.math.games.tree.SequenceForm.InvalidPlayerException;
import lse.math.games.tree.SequenceFormSolver;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Wan Huang
 */
@SuppressWarnings("serial")
public class SequenceLrsServlet extends AbstractRESTServlet 
{
	private static final Logger log = Logger.getLogger(TreeServlet.class.getName());

	private SequenceFormSolver solver = new SequenceFormSolver();	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{		
		response.setContentType("text/plain");
		log.info("Processing new request");
		SequenceForm seqForm = null;
		Equilibria eqs = null;
		try {
			// 0. See if we have a seed for random priors		
			Long seed = this.parseRandomSeed(request.getParameter("s"));

			// 1. pull XML out of request returning any errors
			String xmlStr = request.getParameter("g");
			if (xmlStr == null) {
				this.addError(request, "g parameter is missing");			
				return;
			} else {

				// 2. load XML into ExtensiveForm returning any errors
				ExtensiveForm tree = null;
				try {
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
					ExtensiveFormXMLReader reader = new ExtensiveFormXMLReader();
					tree = reader.load(doc);
					
				} catch (ParserConfigurationException ex) {
					this.addError(request, "unable to configure xml parser");			
				} catch (SAXException ex) {
					this.addError(request, "unable to parse xml");			
				}

				if (tree != null) 
				{
					System.out.println("Huang's Algorithm (2011) ====>");
					eqs = solver.extensiveFormSolver(tree);
				}
			}
		} catch (Exception ex) {
			this.addError(request, ex.toString());
			ex.printStackTrace();			
		}


		try {
		this.writeResponseHeader(request, response);
		if (eqs != null) 
			formatPrint(eqs, response.getWriter());
		} catch (Exception ex) {
			response.getWriter().println(ex.getMessage());
		}
	}
	
	
	public static void formatPrint(Equilibria equ, PrintWriter out){
		Iterator<Equilibrium> it = equ.iterator();
		Equilibrium curr;
		Rational[] v1ext, v2ext;
		Rational payoff1, payoff2;
		int n, n1, n2;
		n = 1;
		
		while (it.hasNext()){
			curr = it.next();
			v1ext = curr.probVec1;
		    v2ext = curr.probVec2;
		    payoff1 = curr.payoff1;
		    payoff2 = curr.payoff2;
		    n1 = curr.getVertex1();
		    n2 = curr.getVertex2();
		
		    out.print(String.format("EE %d P1:(%d) ", n++, n1));
		    for (int i = 0; i < v1ext.length; ++i)
			out.print(v1ext[i].toString() + " ");
		    out.print("EP=" + payoff1.toString());
		    out.print(String.format(" P2=(%d) ", n2));
		    for (int i = 0; i < v2ext.length; ++i)
 			out.print(v2ext[i].toString() + " ");
    		out.print("EP=" + payoff2.toString());
	    	out.println();
		}
	}
}

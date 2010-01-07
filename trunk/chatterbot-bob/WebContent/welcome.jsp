<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
><%
	// <% has to be ON 1. LINE, otherwise servlet will generate blank lines !!! 
	response.setContentType("text/html;charset=UTF-8");

	// Set to expire far in the past. 
	response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
	// Set standard HTTP/1.1 no-cache headers. 
	response.setHeader("Cache-Control",
			"no-store, no-cache, must-revalidate");
	// Set IE extended HTTP/1.1 no-cache headers (use addHeader). 
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	// Set standard HTTP/1.0 no-cache header. 
	response.setHeader("Pragma", "no-cache");
	// ?? needed?
	response.setDateHeader("Last-Modified", System.currentTimeMillis());

	//< % Needed for htmlescape() % >
	// < % @ include file="htmlescape.jsp" % >
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Ask BoB</title>
<link rel="stylesheet" type="text/css" href="bob.css" media="screen" />

<!--  <script type="text/javascript" src="jquery-1.2.6.min.js"></script> -->
<script type="text/javascript" src="http://www.google.com/jsapi"></script>

<script type="text/javascript">
  // Load jQuery
  google.load("jquery", "1");
</script>

<%-- <script type="text/javascript" src="jquery.corner.js"></script> --%>
<script type="text/javascript" src="jquery.nospam.js"></script>
<script language="JavaScript" type="text/javascript"
	src="<%="ajax-chat.js?" + System.currentTimeMillis()%>"></script>


<%
if (request.getParameter("debug") != null
		&& (request.getParameter("debug").equals("yes"))) { %>
<!--  only loaded in local version!  -->
<script language="JavaScript" type="text/javascript"
	src="<%="startChatLocal.js?" + System.currentTimeMillis()%>"></script>
<% } %>


</head>
<body>
<div class="bob_main_wrap">
	
		<div class="bob_main_right">

		
				<div class="bob_bubble_wrap">
				
					<div class="bob_bubble">
					
						<!-- @I-CT in dieses Div werden Bob Dialog / Chatfenster einf├╝gen | Dummy Text l├╢schen  -->
						<div class="bob_bubble_text" id="chatDiv">
							 
						</div>
						
					</div>
				
				</div>
				
				<div class="bob_input">
				

				


					<!-- @I-CT Dummy Formular genutzt | richtige Values und Pfade noch einzutragen -->
					<form accept-charset="UTF-8" id="frmmain" name="frmmain" onsubmit="return blockSubmit();" action="">
						<input type="hidden" name="name" id="name" value="You" />
						<input type="hidden" name="last" id="last" value="0"/>
						<fieldset class="bob_language">

							<strong>Choose the language:</strong>

							<input class="bob_radiobutton" type="radio" name="lang" value="EN" 
	<%if (request.getParameter("language") == null
					|| (!request.getParameter("language").equals("de") && !request
							.getParameter("language").equals("it"))) {%>
	checked="checked" <%}%> /> English
							 
							<input class="bob_radiobutton" type="radio" name="lang" value="DE" 	<%if (request.getParameter("language") != null
					&& request.getParameter("language").equals("de")) {%>
	checked="checked" <%}%> /> Deutsch
	

	<input class="bob_radiobutton" type="radio" name="lang" value="IT" 	<% if (request.getParameter("language") != null
					&& request.getParameter("language").equals("it")) {%>
	checked="checked" <%} %> /> Italiano  
						
						</fieldset>
						
						<fieldset class="bob_question">
							
							<input class="input" type="text" id="txtMessage" name="message" /> 
							<input class="submit" type="button" name="btnSendChat"
						id="btnSendChat" value="Send" onclick="javascript:sendChatText();" />
						</fieldset>
						
					</form>
				
				</div>
		
		</div>

		<!-- @I-CT in diesem DIV werden die verschiedenen Bilder mti den Gef├╝hlsausdr├╝cken des BoB Avatars eingef├╝gt | Bilder bob01.gif - bob10.gif befinden sich im img Ordner -->
		<div class="bob_main_left"><img id="face" src="img/bob01.gif" alt="Emotional face" width="103px" height="126px" /></div>

	</div>



<%
	if (request.getParameter("debug") != null
			&& (request.getParameter("debug").equals("yes"))) {
%>
<form id="frmreload" name="frmreload" action="">
<input type="button" name="btnReloadTT" id="btnReloadTT"
	value="Reload TT and AbbrevFiles"
	onclick="javascript:reloadTT();" />
<input type="button" name="btnResetTT" id="btnResetTT"
	value="Reset dialogue history"
	onclick="javascript:resetChat();" />

</form>
<iframe id="logReader"
	style="clear: both; left: 0px; float: left; width: 1100px; height: 550px; margin-right: 0; position: relative; top: 0px"
	name="logReader" src="logReader.jsp" frameborder="0" scrolling="no"></iframe>
<%
	} else if (request.getParameter("popup") != null
			&& (request.getParameter("popup").equals("yes"))) {
%>

<br/>
<br/>

<%if (request.getParameter("language") == null
					|| (!request.getParameter("language").equals("de") && !request
							.getParameter("language").equals("it"))) {%>
<h3>Some topics to ask BoB about:</h3>
<p>Infrastructure and organization of the University Library,
lending, ordering, borrowing, reserving, picking up, returning, renewing
books etc. Searching OPAC, books, journals, and articles. Printing,
reading, photocopying articles etc.</p>
<div class="msgList">
<p class="msgHead">Example questions (click to expand)</p>
<div class="msgBody">
<ul>
	<li>I want a book which is not available in South Tyrol</li>
	<li>I need an article from interlibrary loan</li>
	<li>what is the password for ordering books?</li>
	<li>When can I return a book?</li>
	<li>what do I have to do to renew a book?</li>
	<li>Will I be informed when the books are there?</li>
	<li>I want to reserve a DVD!</li>
	<li>How can I order books?</li>
	<li>what's the amount of books one can check out?</li>
	<li>I need to search for journal articles</li>
	<li>I want to read the newspaper on the computer</li>
	<li>Does the library have journals that can be consulted online?</li>
	<li>where do you keep the textbooks?</li>
	<li>what does the status "can't be borrowed" mean?</li>
	<li>which databases do exist?</li>
	<li>Is the library open tomorrow?</li>
	<li>How do I know if a book I've suggested is available?</li>
	<li>what's the best way to find DVDs?</li>
	<li>where can I print?</li>
	<li>how much do I pay for one photocopy?</li>
	<li>I need a carrel for the month of July</li>
</ul>
</div>
</div>
<br/>
<br/>
Want to know more about BoB? Go to the <a href="http://www.unibz.it/en/library/about/projects/bob-project.html">BoB project description page</a>. 
<br/>Comments, questions, suggestions? Please contact <a href="" rel="ti/zbinu/fni//renhcsrik" class="email"></a>.
<%} // end language-specific example questions %> 



<%if (request.getParameter("language") != null
					&& request.getParameter("language").equals("de")) {%>
<h3>Dar├╝ber kann man mit BoB reden:</h3>
<p>Infrastruktur und Organisation der Universit├дtsbibliothek. Ausleihen, vormerken, verl├дngern, zur├╝ckgeben von Medien. Recherche im online Katalog. Suche von B├╝chern, Zeitschriften und Artikeln. Drucken und fotokopieren. U.v.m.</p>
<div class="msgList">
<p class="msgHead">Beispielfragen (hier klicken)</p>
<div class="msgBody">
<ul>
<li>Ich m├╢chte Infos ├╝ber den Research Publication Server</li>
<li>Was kostet ein Bestellung ├╝ber Fernleihe?</li>
<li>Wie bekomme ich B├╝cher, die in keiner s├╝dtiroler Bibliothek verf├╝gbar sind?</li>
<li>Ist die Bibliothek am Samstag Vormittag offen?</li>
<li>Wie kann ich ├╝ber Internet ein Buch verl├дngern?</li>
<li>Wollte fragen, ob der Dienst des wireless LAN auch f├╝r Externe Nutzerinnen m├╢glich ist?</li>
<li>Muss ich die B├╝cher selbst abgeben oder kann das auch meine Schwester f├╝r mich erledigen?</li>
<li>Habe leider mein Passwort vergessen und es f├дllt mir auch beim besten Willen nicht ein</li>
<li>Die Leihfrist f├╝r die ausgeliehenen B├╝cher ist abgelaufen, ich m├╢chte sie verl├дngern</li>
<li>die Zeitschriften, m├╝ssen die in der Bibliothek bleiben, oder sind sie ausleihbar?</li>
<li>Ich m├╢chte die Dolomiten ausborgen</li>
<li>Ich m├╢chte eine DVD ausleihen, wie mache ich das am besten?</li>
<li>Wie reserviere ich ein Buch?</li>
<li>Ich ben├╢tige Zeitschriftenartikel, wie finde ich diese?</li>
<li>die e-Zeitschriften, kann ich die auch von zu Hause an meinem pc lesen</li>
<li>Wie sucht man am besten nach einem Buch?</li>
<li>Wo ist denn die Lehrbuchsammlung?</li>
<li>Was hei├Яt es, wenn ein Medium den Status "Reserved" hat?</li>
<li>Welche Online Ressourcen gibts in der Bibliothek?</li>
<li>Wie kann ich eure Datenbanken finden?</li>
<li>wo finde ich im Internet den opac?</li>
<li>Ich suche B├╝cher f├╝r Kinder und Jugendliche!</li>
<li>Hat die Bibliothek auch zu Ostern auf?</li>
<li>Wo finde ich die Diplomarbeiten der Studierenden der Universit├дt Bozen?</li>
<li>wo kann ich die DVDs in Bozen finden?</li>
<li>Gibt es in der Bibliothek Computer mit einem Internetanschluss?</li>
</ul>
</div>
</div>
<br/>
<br/>
M├╢chtest Du mehr ├╝ber BoB erfahren? Hier findest Du eine detaillierte <a href="http://www.unibz.it/en/library/about/projects/bob-project.html">Projektbeschreibung</a>. 
<br/>Kommentare, Fragen, Anregungen? Kontakt: <a href="" rel="ti/zbinu/fni//renhcsrik" class="email"></a>.
<%} 

if (request.getParameter("language") != null
					&& request.getParameter("language").equals("it")) {%>
<h3>Con me puoi parlare di questo:</h3>
<p>Organizzazione della biblioteca universitaria, spazi e attrezzature, prestito, restituzione e prenotazione di libri e altro materiale, proroghe, consultazione del catalogo online, ricerca di libri, riviste e articoli, stampe e fotocopie. </p>
<div class="msgList">
<p class="msgHead">Clicca qui se vuoi leggere alcuni esempi di come formulare le domande:</p>
<div class="msgBody">
<ul>
<li>La biblioteca offre accesso gratuito a internet?</li>
<li>Quanto costa una fotocopia? </li>
<li>Come faccio a stampare un documento?</li>
<li>Quanto costa una stampa?  </li>
<li>Ci sono fotocopiatrici a colori? </li>
<li>Quanto costa affittare un armadietto? </li>
<li>A quale piano si trovano i dvd?</li>
<li>Posso suggerire alla biblioteca lтАЩacquisto di un libro?</li>
<li>Dove si trovano i libri della bibliografia dei corsi?</li>
<li>Cosa significa at disposal? </li>
<li>├И possibile prendere in prestito le tesi di laurea?</li>
<li>Vorrei sapere gli orari di apertura al pubblico della biblioteca in questa settimana</li>
<li>Dove trovo i dizionari on line? </li>
<li>├И possibile accedere ai database anche da casa?</li>
<li>Cosa significa lo status reserved?</li>
<li>Dove posso trovare le vostre riviste in versione elettronica?</li>
<li>├И possibile consultare anche quotidiani online?</li>
<li>Come si esegue una ricerca per argomento tramite lтАЩOPAC?</li>
<li>Devo cercare degli articoli.</li>
<li>Quanti DVD si possono prendere in prestito contemporaneamente?</li>
<li>Come si prenota un libro?</li>
<li>├И possibile prendere in prestito un quotidiano?</li>
<li>Si deve pagare per prenotare un libro? </li>
<li>Quanto si paga per lтАЩiscrizione alla biblioteca?</li>
<li>Sai dirmi come trovo il numero della library card?   </li>
<li>Ho la possibilit├а di prorogare da solo i miei prestiti?</li>
<li>Ho provato a prorogare la scadenza dei miei prestiti tramite internet, ma con poco successo</li>
<li>Ho dimenticato la mia password per la biblioteca. </li>
<li>Vorrei ordinare un libro tramite il servizio di prestito interbibliotecario.</li>
</ul>
</div>
</div>
<br/>
<br/>
Vuoi sapere di pi├╣ su BoB? Visita il sito del <a href="http://www.unibz.it/en/library/about/projects/bob-project.html">progetto BoB</a>. 
<br/>Hai commenti, domande, suggerimenti? Contatta <a href="" rel="ti/zbinu/fni//renhcsrik" class="email"></a>.
<%} // end language-specific example questions %>





<script type="text/javascript">
$(document).ready(function()
                {
                  //hide the all of the element with class msg_body
                  $(".msgBody").hide();
                  //toggle the componenet with class msg_body
                  $(".msgHead").click(function()
                  {
                    $(this).next(".msgBody").slideToggle(600);
                  });

                  $('a.email').nospam({
                      replaceText: true,    // BOOLEAN, optional default false. If set to true, replaces matched elements' text with the e-mail address
                      filterLevel: 'normal' // STRING, optional accepts 'low' or 'normal', default 'normal'.
                                            // low: email/domain/tld
                                            // normal: dlt/niamod/liame (email/domain/tld reversed)
                    });
              
		  
                });
</script>

<%
	} // end "popup" parameter
%>

<script type="text/javascript">
<%
if (request.getParameter("debug") != null
		&& (request.getParameter("debug").equals("yes"))) { %>
	startChatLocal();
	<%}  else { %>
	startChat();	
	<% }  %>
	
</script>

</body>
</html>

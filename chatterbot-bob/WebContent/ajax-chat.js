// switch between old and new CMS: if bobArray is defined, we're in SharePoint
var isSharePoint = typeof bobArray != 'undefined';


var sBobApplication = isSharePoint ? 'BobXMLHandler.customHandler'
		: 'getChat.jsp';
var sImagePath = isSharePoint ? '/_styles/Images/webparts/' : 'img/';
var sImageFileExtension = '.gif';

var sNameBob = 'BoB';
var hiddenUser = "rating";

// Bob emotions
var NORMAL = '[01]';
var SORRY = '[07]';

var textFieldId = isSharePoint ? bobArray.textFieldId : '#txtMessage';
var chatDivId = isSharePoint ? bobArray.chatDivId : '#chatDiv';
var faceId = isSharePoint ? bobArray.faceId : '#face';
// TODO new element in bobArray
var nameId = isSharePoint ? bobArray.nameId : '#name';
// TODO new element in bobArray
var lastId = isSharePoint ? bobArray.lastId : '#last';
// TODO new element in bobArray
var langInputName = isSharePoint ? bobArray.langInputName : 'lang';
// TODO new element in bobArray
// var bobCommentId = isSharePoint ? bobArray.bobCommentId : '#bobCommentId';
// TODO new element in bobArray
var bobFormId = isSharePoint ? bobArray.bobFormId : '#frmmain';

var evalLinkCntId = isSharePoint ? bobArray.bobArray.evalLinkCntId
		: '#evalLinkCntId';
// user messages
var badRatingLinkContentEN = " <span id='evalCnt'>[Bad answer?]</span>";
var badRatingLinkContentDE = " <span id='evalCnt'>[Schlechte Antwort?]</span>";
var badRatingLinkContentIT = " <span id='evalCnt'>[Risposta sbagliata?]</span>";

function badRatingLink(lang) {
	if (lang == "EN") {
		return "<span id='evalLinkCntId'><a class='evalLink' id='evalLinkId' onclick='submitEvaluation(1)' title='Click here if this answer is bad'>"
				+ badRatingLinkContentEN + "</a></span>";
	}
	if (lang == "DE") {
		return "<span id='evalLinkCntId'><a class='evalLink' id='evalLinkId' onclick='submitEvaluation(1)' title='Hier klicken wenn diese Antwort nicht passt'>"
				+ badRatingLinkContentDE + "</a></span>";
	}
	if (lang == "IT") {
		return "<span id='evalLinkCntId'><a class='evalLink' id='evalLinkId' onclick='submitEvaluation(1)' title='Clicca qui se la risposta  sbagliata'>"
				+ badRatingLinkContentIT + "</a></span>";
	}
}

function ratingFeedback(lang) {
	if (lang == "EN") {
		return " <span style='font-weight: bold'>[Thank you]</span>";
	}
	if (lang == "DE") {
		return " <span style='font-weight: bold'>[Danke]</span>";
	}
	if (lang == "IT") {
		return " <span style='font-weight: bold'>[Grazie]</span>";
	}
}

// var bobApologize = "Thanks for your help.";

// html elements for dialogue and evaluation
function dialogEntry(name, text) {
	return "<div class='chat" + name + " round" + name
			+ "' ><span class='chatName'>" + name + ":</span>&nbsp;" + text
			+ "</div>";
}

/**
 * function commentEntry(comment) { return "<div class='bobComment'>" + comment + "</div>"; }
 */

// push the startChat method in the Sharepoint onLoad
if (isSharePoint) {
	_spBodyOnLoadFunctionNames.push("startChat");
}

// MK 08.09.08: deactivate server polling to minimize network load.
// var mTimer;
// MK 08.09.08: deactivate server polling to minimize network load.
// var refreshTime = 3000;

// startChat gets called upon reset (or page reload)
function startChat() {
	
	

	
	
	
	
	
	// recover last ID stored in hidden field (useful on page reload in active
	// session)
	//lastMessage = $('input' + lastId).val();
	lastMessage = $('input[name=last]').val();

	// reset UI
	$(chatDivId).empty();
	$(textFieldId).val('');
	updateBobEmotion(NORMAL);
	// Set the focus to the Message Box.
	$(textFieldId).focus();
	// Start Receiving Messages.
	//var langparam = $('input[name=' + langInputName + ']:checked').val();
	var langparam = $('input[name=lang]:checked').val();

	// $.post(sBobApplication, 'last=' + lastMessage,
	$.post(sBobApplication, 'lang=' + langparam + '&last=' + lastMessage,
			function(xml) {
				handleReceiveChat(xml);
			}, "xml");
	return false;
}

// Add the message in the arg to the chat server.
function sendChatTextFromClickedLink(textFromLink) {
	$(textFieldId).val(textFromLink);
	sendChatText();
}

// Add a message to the chat server.
function sendChatText() {
	$(textFieldId).val($(textFieldId).val() ? $(textFieldId).val() : " ");
	// in case of sharepoint the string to provide is something like
	// name=You&last=0&lang=EN&message=bla
	var param = $(bobFormId).serialize();
	$.post(sBobApplication, param, function(xml) {
		handleReceiveChat(xml);
	}, "xml");
	// clear the text field
	$(textFieldId).val('');
	// clear the bob comment (apologize etc.)
	// $(bobCommentId).html('&nbsp;');
	// hide old evaluation links
	$(".evalLink").remove();
	// Set the focus to the Message Box.
	$(textFieldId).focus();
}

// Function for handling the return of chat text
function handleReceiveChat(xmldoc) {
	// DEBUG
	if (xmldoc == null)
		$(chatDivId).innerHTML += "ERROR: xmldoc==null";
	lastMessage = $('input' + lastId).val();
	userName = $('input' + nameId).val();
	$("message", xmldoc).each( function() {
		// check if this message is indeed not in the user's browser yet ...
			if ($(this).attr("id") <= lastMessage) {
				return;
			}
			var user = $("user", this).text();
			var time = $("time", this).text();
			var text = $("text:first", this) ? $("text:first", this).text()
					: "";

			switch (user) {
			// Note the server always returns messages with user name "BoB"
			// for BoB's messages
			case (userName):
				text = text.replace(/</g, '&lt;').replace(/>/, '&gt;');
			case (sNameBob):
				text = updateBobEmotion(text);
				$(chatDivId).append(dialogEntry(user, text));
				break;
			case (hiddenUser):
				// got a rating message in the XML
				// just ignore this;
			default:
				// should never happen
				break;
			}
		});

	lastMessage = $("message:last", xmldoc).attr("id");
	// store also in hidden form
	$(lastId).val(lastMessage);

	// show the evaluation link if the last message wasn't already evaluated
	if ($("message:last", xmldoc).find("user").text() != hiddenUser
			&& $("message", xmldoc).length > 1) {
		showEvaluationLink();
	}
	// simulate balloon
	/**
	 * $('.round' + sNameBob).corner("round top bl 6px").corner("cool br 8px");
	 * $('.round' + userName).corner("top br 6px").corner("cool bl 8px");
	 * $('.chat' + sNameBob).removeClass('round' + sNameBob); $('.chat' +
	 * userName).removeClass('round' + userName);
	 */

	// need to call twice otherwise IE6 is stuck in the middle of the scrolling
	$(chatDivId).scrollTop($(chatDivId).attr('scrollHeight'));
	$(chatDivId).scrollTop($(chatDivId).attr('scrollHeight'));
	return false;

	// Refresh chat to get any new messages from server
	// MK 08.09.08: deactivate server polling to minimize network load.
	// mTimer = setTimeout('getChatText();', refreshTime);
}

// this function updates bob's face and trims the emotion from text
function updateBobEmotion(text) {
	emoRe = new RegExp("^\[[0-9]+\]");
	if (emoRe.test(text)) {
		var emo = text.match(emoRe);
		var emo_code = /[0-9]+/.exec(text.match(emoRe));
		$(faceId).attr('src',
				sImagePath + 'bob' + emo_code + sImageFileExtension);
		text = text.substring(text.search(emoRe) + 4);
	}
	return text;
}

// Handles the user submitting text via enter key. (Invoked by "onsubmit"
// attribute of form element.) Instead of submitting the
// form, send a new message to the server and return false.
function blockSubmit() {
	sendChatText();
	return false;
}

// Evaluation code follows
function showEvaluationLink() {
	clearEvaluation();
	
	var langparam = $('input[name=' + langInputName + ']:checked').val();
	$("div:last", chatDivId).append(badRatingLink(langparam));
}

function submitEvaluation(evalValue) {
	var param = 'message=' + evalValue;
	param += '&name=' + hiddenUser;
	param += '&lang=XX';
	$.post(sBobApplication, param, function() {
		updateBobEmotion(SORRY);
		// $(bobCommentId).html(commentEntry(bobApologize)); //.corner("4px");
			//var langparam = $('input' + langInputName + ':checked').val();
			var langparam = $('input[name=' + langInputName + ']:checked').val();
			$(evalLinkCntId).html(ratingFeedback(langparam));
			// clearEvaluation();
		}, "xml");
}

function clearEvaluation() {
	// $(".evalLink").hide();
	$(evalLinkCntId).remove();
}
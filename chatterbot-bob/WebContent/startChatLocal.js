/*******************************************************************************
 * Local version (triggered by welcome.jsp)
 * 
 * Here, startChat() overrides some global variables
 ******************************************************************************/

function startChatLocal() {

	
	// recover last ID stored in hidden field (useful on page reload in active session)
	lastMessage = $('input[name=last]').val();
	
	// reset UI
	$(chatDivId).empty();
	$(textFieldId).val('');
	updateBobEmotion(NORMAL);
	// Set the focus to the Message Box.
	$(textFieldId).focus();
	// Start Receiving Messages.
	var langparam = $('input[name=lang]:checked').val();
	$.post(sBobApplication, 'lang=' + langparam + '&last=' + lastMessage,
			function(xml) {
				handleReceiveChat(xml);
			}, "xml");
	return false;
}

// Debug mode only: Reloads TT and abbrev files
function reloadTT() {
	// recover last ID stored in hidden field (useful on page reload in active session)
	lastMessage = $('input[name=last]').val();
	
	$.post(sBobApplication, '?last=' + lastMessage + '&action=reloadTT',
			function(xml) {
				startChat();
			}, "xml");
}

// Debug mode only: Cleans the database so we can start a new chat session.
function resetChat() {
	// recover last ID stored in hidden field (useful on page reload in active session)
	lastMessage = $('input[name=last]').val();
	
	$.post(sBobApplication, '?last=' + lastMessage + '&action=reset', function(
			xml) {
		startChat();
	}, "xml");
}

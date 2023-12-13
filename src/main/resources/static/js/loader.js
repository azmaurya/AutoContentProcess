function ButtonClicked() {
	var x = document.getElementById('content_id').value;
	var xmlhttp;
	if (x.length >= 8) {
		document.getElementById('formsubmitbutton').style.display = 'none';
		document.getElementById('buttonreplacement').style.display = '';
	}
}
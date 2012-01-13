function show(id) {
	document.getElementById(id).style.display="";
}

function hide(id) {
	document.getElementById(id).style.display="none";
}

function showOnly(id) {
	var divs = document.getElementsByTagName('div');
	for (var i=0; i < divs.length; i++) {
		if (divs[i].className == 'div') {
			hide(divs[i].id);
		}
	}
	show(id);
}
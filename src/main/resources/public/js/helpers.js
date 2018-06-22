function closePopover() {
	document.getElementsByClassName("popover")[0].style.visibility = 'hidden';
}

var expandRow = function(ele) {
	debugger;
	if (ele.parentElement.parentElement.parentElement.childNodes[3].style.display != 'none') {
		ele.parentElement.parentElement.parentElement.childNodes[3].style.display='none';
	} else { ele.parentElement.parentElement.parentElement.childNodes[3].style.display=''}
}

function clearSearchText() {
	document.getElementById("searchTextBox").value = '';
}

function clearFavoriteBox() {
	document.getElementById("favoriteBox").value = '';
}

function lowerCaseF(a){
    setTimeout(function(){
        a.value = a.value.toLowerCase();
    }, 1);
}
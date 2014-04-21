$(function() {

	var toc = $("#toc").tocify({
		selectors : "h2,h3,h4,h5"
	}).data("toc-tocify");

	//                prettyPrint();
	$(".optionName").popover({
		trigger : "hover"
	});

	var pos = {
		my : 'top center', // Position my top left...
		at : 'bottom left', // at the bottom right of...
		target : 'mouse', // Position it where the click was...
		adjust : {
			mouse : false
		} // ...but don't follow the mouse
	};
	var tpStyle = {
		classes : 'qtip-tipsy'
	};
	// Grab some elements to apply the tooltip to
	$('#linkToFile').qtip({
		content : {
			text : 'אפשר להוריד אותו <a href="http://dumps.wikimedia.org/hewiktionary/latest">מכאן</a>.<br>אבל הוא כלול בrepository אז לא צריך.<br>ובכלל - אם אין לכם סיבה טובה להוריד אותו, אל תורידו אותו כדי לא ליצור עומס על השרתים של&nbsp;ויקימדיה.'
		},
		position : pos,
		style : tpStyle,
		hide : {
			event : 'click'
		}
	});

$('#tp-wikimedia').qtip({
		content : {
			text : 'קרן בין לאומית המפעילה את ויקימילון, ויקיפדיה ומספר מיזמי וויקי נוספים.'
		},
		position : pos,
		style : tpStyle,
		hide : {
			event : 'click'
		}
	});
});

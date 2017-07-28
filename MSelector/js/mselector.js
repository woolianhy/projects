window.MSelector = (function(){
	
	"use strict";

	var mSelector = function(param){
		this._element = null;
		this._columnElements = [];
		this._columnPosMap = {};
		this.template = {
			btn_ok: '\u786e\u5b9a',
			btn_cancel: '\u53d6\u6d88'
		};
		this.columnNum = 0;
		this.data = [];
		this.selectedData = [];

		this.onselected = null;
		this.oncolumnchange = null;

		if(param){
			this.init(param);
		}
	};
	mSelector.prototype.init = function(param){
		var that = this;
		this.onselected = param.onselected || null;
		this.oncolumnchange = param.oncolumnchange || null;
		this.columnNum = param.columnNum || 0;
		this._element = document.createElement('div');
		this._element.setAttribute('class', 'mselector');
		this._element.innerHTML = '<div class="picker">' +
				'<div class="tools-box">' +
				'<div class="cancel">' + this.template.btn_cancel + '</div>' +
				'<div class="ok">' + this.template.btn_ok + '</div>' +
				'<div class="waiting-wrapper">' +
				'<div class="wait o1"></div>' +
				'<div class="wait o2"></div>' +
				'<div class="wait o3"></div>' +
				'</div>' +
				'</div>' +
				'<div class="content-wrapper"><div class="content-selected"></div><div class="content"></div></div>' +
				'</div>';
		this._element.style.display = 'none';
		this._element.querySelector('.ok').addEventListener('touchstart', function(){
			if(that.onselected){
				that.onselected(that.selectedData);
			}
			that.hide();
		});
		this._element.querySelector('.cancel').addEventListener('touchstart', function(){that.hide();});


		var that = this;

		var contentElement = this._element.querySelector('.content');
		for(var i = 0; i < this.columnNum; i++){
			this.selectedData.push('');
		}
		var htmlStr = '';
		var columnWidth = Math.floor(1000 / this.columnNum) / 10;
		for(var i = 0; i < this.columnNum; i++){
			if(param.columnStyles){
				htmlStr += '<div class="column" style="' + param.columnStyles[i] + '"></div>';
			}else{
				htmlStr += '<div class="column" style="width:' + columnWidth + '%"></div>';
			}
		}
		contentElement.innerHTML = htmlStr;
		
		this._columnElements = contentElement.childNodes;
		for(var i = 0, len = this._columnElements.length; i < len; i++){
			this._columnElements[i].setAttribute('columnIdx', i);
			this._columnElements[i].addEventListener('touchstart', function(e){
				e.preventDefault();
				var target = e.target;
				var columnIdx = this.getAttribute('columnIdx');
				var posParam = that._columnPosMap[columnIdx + ''];
				if(!posParam){
					posParam = {};
					that._columnPosMap[columnIdx + ''] = posParam;
				}
				posParam['begin-y'] = e.targetTouches[0].screenY;
				posParam['begin-top'] = posParam['current-y'] || 0;
			});
			this._columnElements[i].addEventListener('touchmove', function(e){
				e.preventDefault();
				var target = e.target;
				var screenY = e.targetTouches[0].screenY;
				var columnIdx = this.getAttribute('columnIdx');
				var beginY = that._columnPosMap[columnIdx + '']['begin-y'];
				var beginTop = that._columnPosMap[columnIdx + '']['begin-top'];
				var top = (beginTop + screenY - beginY);
				var posParam = that._columnPosMap[columnIdx + ''];
				posParam['current-y'] = top;
				// this.style.top = top + 'px';
				this.style["-webkit-transform"] = 'translate3d(0,' + top + 'px,0)';
			});
			this._columnElements[i].addEventListener('touchend', function(e){
				e.preventDefault();
				var target = e.target;
				var result = gearTooth(contentElement, this, that._element.querySelector('.content-selected'));
				var columnIdx = parseInt(this.getAttribute('columnIdx'), 10);
				var posParam = that._columnPosMap[columnIdx + ''];
				posParam['current-y'] = result.top;
				that.selectedData[columnIdx] = this.childNodes[result.targetIdx].getAttribute('item-code');
				if(that.oncolumnchange){
					that.oncolumnchange(columnIdx, result.targetIdx, that.selectedData[columnIdx]);
				}
			});

		}

		document.body.appendChild(this._element);
	};
	mSelector.prototype.showWaiting = function(){
		this._element.querySelector('.waiting-wrapper').style.display = 'block';
	};
	mSelector.prototype.hideWaiting = function(){
		this._element.querySelector('.waiting-wrapper').style.display = 'none';
	};
	mSelector.prototype.show = function(){
		this._element.style.display = 'block';
	};
	mSelector.prototype.hide = function(){
		this._element.style.display = 'none';
	};
	mSelector.prototype.loadContent = function(data){
		for(var i = 0, len = data.length; i < len; i++){
			if(i >= this.columnNum){
				break;
			}
			var htmlStr = '';
			for(var j = 0, lenJ = data[i].length; j < lenJ; j++){
				htmlStr += '<div class="item" item-code="' + data[i][j].code + '">' + data[i][j].value + '</div>';
			}
			this._columnElements[i].innerHTML = htmlStr;
		}
	};
	mSelector.prototype.loadColumn = function(colIdx, data){
		if(colIdx >= this.columnNum){
			return;
		}
		this._columnElements[colIdx].innerHTML = '';
		var htmlStr = '';
		for(var j = 0, lenJ = data.length; j < lenJ; j++){
			htmlStr += '<div class="item" item-code="' + data[j].code + '">' + data[j].value + '</div>';
		}
		this._columnElements[colIdx].innerHTML = htmlStr;
	};
	mSelector.prototype.setItemSelected = function(colIdx, itemIdx){
		if(colIdx >= this._columnElements.length){
			return;
		}
		if(itemIdx >= this._columnElements[colIdx].childNodes.length){
			return;
		}
		var targetElement = this._element.querySelector('.content-selected');
		var contentTop = this._element.querySelector('.content').getBoundingClientRect().top;
		var elementTop = this._columnElements[colIdx].getBoundingClientRect().top;
		var targetTop = targetElement.getBoundingClientRect().top;
		var height = targetElement.getBoundingClientRect().height;
		var offset = targetTop - contentTop;
		var top = (offset - height * itemIdx);
		var posParam = this._columnPosMap[colIdx + ''];
		if(!posParam){
			posParam = {};
			this._columnPosMap[colIdx + ''] = posParam;
		}
		posParam['current-y'] = top;
		// this._columnElements[colIdx].style.top = top + 'px';
		this._columnElements[colIdx].style["-webkit-transform"] = 'translate3d(0,' + top + 'px,0)';
		this.selectedData[colIdx] = this._columnElements[colIdx].childNodes[itemIdx].getAttribute('item-code');
		if(this.oncolumnchange){
			this.oncolumnchange(colIdx, itemIdx, this.selectedData[colIdx]);
		}
	};
	mSelector.prototype.getSelectedData = function(){
		return this.selectedData;
	};
	function gearTooth(contentElement, element, targetElement){
		var len = element.childNodes.length;
		var contentTop = contentElement.getBoundingClientRect().top;
		var elementTop = element.getBoundingClientRect().top;
		var targetTop = targetElement.getBoundingClientRect().top;
		var height = targetElement.getBoundingClientRect().height;

		var offset = targetTop - contentTop;
		var targetIdx = Math.round((contentTop - elementTop + offset) / height);
		if(targetIdx < 0){
			targetIdx = 0;
		}
		if(targetIdx > len - 1){
			targetIdx = len - 1;
		}
		var top = offset - height * targetIdx;
		// element.style.top = top + 'px';
		element.style["-webkit-transform"] = 'translate3d(0,' + top + 'px,0)';
		return {targetIdx: targetIdx, top: top};
	}
	return mSelector;
})();

window.MCalendar = (function(){
	var mCalendar = function(param){
		this.trigger = null;
		this.target = null;
		this.selector = null;

		if(param){
			this.init(param);
		}
	};
	mCalendar.prototype.init = function(param){
		var that = this;
		this.trigger = param.trigger || null;
		this.target = param.target || null;
		this.selector = new MSelector({
			columnNum: 3,
			columnStyles: ['width:34%;text-align:right;','width:33%;','width:33%;text-align:left;'],
			onselected: function(data){
				document.querySelector(that.target).value = data.join('-');
			},
			oncolumnchange: function(columnIdx, dataIdx, data){
				var currData = this.getSelectedData();
				if('' == currData[0] || '' == currData[1] || '' == currData[2]){
					return;
				}
				if(0 == columnIdx || 1 == columnIdx){
					var data = [];
					var date = new Date(parseInt(currData[0], 10), parseInt(currData[1], 10) - 1, 1);
					var recentDay = parseInt(currData[2], 10);
					var maxDay = getLastDayOfMonth(date);
					for(var i = 0; i < maxDay; i++){
						var code = (i + 1) + '';
						data.push({code: code, value: code + ' \u65e5'});
					}
					this.loadColumn(2, data);
					if(recentDay > maxDay){
						recentDay = maxDay;
					}
					this.setItemSelected(2, recentDay - 1);
				}
			}
		});
		var calData = [[],[],[]];
		var cal = new Date();
		var year = cal.getFullYear();
		var month = cal.getMonth() + 1;
		var day = cal.getDate();

		//FIXME default +/- 100 years
		for(var i = 0; i < 200; i++){
			var code = (year - 100 + 1 + i) + '';
			calData[0].push({code: code, value: code + ' \u5e74'});
		}
		for(var i = 0; i < 12; i++){
			var code = (i + 1) + '';
			calData[1].push({code: code, value: code + ' \u6708'});
		}
		for(var i = 0, len = getLastDayOfMonth(cal); i < len; i++){
			var code = (i + 1) + '';
			calData[2].push({code: code, value: code + ' \u65e5'});
		}

		that.selector.loadContent(calData);

		document.querySelector(this.trigger).onclick = function(){
			that.selector.show();
			var yearIdx = 0;
			var monthIdx = month - 1;
			var dayIdx = day - 1;
			var targetValue = document.querySelector(that.target).value;
			if(targetValue){
				var dateArr = targetValue.split('-');
				for(var i = 0, len = calData[0].length; i < len; i++){
					if(dateArr[0] == calData[0][i].code){
						yearIdx = i;
						break;
					}
				}
				monthIdx = parseInt(dateArr[1], 10) - 1;
				dayIdx = parseInt(dateArr[2], 10) - 1;
			}else{
				var yearStr = year + '';
				for(var i = 0, len = calData[0].length; i < len; i++){
					if(yearStr == calData[0][i].code){
						yearIdx = i;
						break;
					}
				}
			}
			that.selector.setItemSelected(0, yearIdx);
			that.selector.setItemSelected(1, monthIdx);
			that.selector.setItemSelected(2, dayIdx);
		};
	};

	function getLastDayOfMonth(date){
		var year = date.getFullYear();
		var month = date.getMonth();
		if(month >= 11){
			year++;
			month = 0;
		}else{
			month++;
		}
		var nextMonthFirstDate = new Date(year, month, 1);
		var targetDate = new Date(nextMonthFirstDate.getTime() - 24 * 3600000);
		return targetDate.getDate();
	}
	return mCalendar;
})();
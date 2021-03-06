/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

// ================================== PUBLIC API FUNCTIONS

// ========== implementation

O$.MonthTable = {
  _init: function(componentId,
                  day, locale, dateFormat, scrollOffset,
                  preloadedEventParams, resources, eventAreaSettings,
                  editable, onchange, editingOptions,
                  stylingParams,
                  uiEvent,
                  calendarOptions,
                  timetableId) {

    var monthTable = O$.initComponent(componentId, null, {
              _viewType: "month"
            });

    if (O$.isExplorer()) {
      if (!monthTable._initScheduled) {
        monthTable._initScheduled = true;
        var initArgs = arguments;
        // postpone initialization to avoid IE failure during page loading
        O$.addInternalLoadEvent(function() {
          O$.MonthTable._init.apply(null, initArgs);
        });
        return;
      }
    }

    monthTable.DEFAULT_EVENT_CLASS_NAME = "o_monthTableEvent";
    monthTable.DEFAULT_EVENT_NAME_CLASS_NAME = "o_monthTableEventName";
    monthTable.DEFAULT_EVENT_CONTENT = [
      {type: "time"},
      {type: "name"},
      {type: "resource"}
    ];
    monthTable.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT = 1;
    monthTable._timetable = O$(timetableId);

    O$.TimeTableView._init(componentId, day, locale, dateFormat, preloadedEventParams, resources,
            eventAreaSettings, editable, onchange, editingOptions, stylingParams, uiEvent);

    var moreLinkElementClass = O$.combineClassNames(["o_moreLinkElement", stylingParams.moreLinkElementClass]);
    var moreLinkClass = O$.combineClassNames(["o_moreLink", stylingParams.moreLinkClass]);
    var moreLinkText = stylingParams.moreLinkText ? stylingParams.moreLinkText : "More...";

    if (!uiEvent)
      uiEvent = {};

    var weekdayHeadersRowClass = O$.combineClassNames(["o_weekdayHeadersRow", stylingParams.weekdayHeadersRowClass]);
    var weekdayStyle = O$.combineClassNames(["o_weekdayText", stylingParams.weekdayClass]);
    var weekdayPattern = stylingParams.weekdayPattern ? stylingParams.weekdayPattern : "EEEE";


    monthTable._escapeEventNames = uiEvent.escapeName !== undefined ? uiEvent.escapeName : true;
    monthTable._escapeEventDescriptions = uiEvent.escapeDescription !== undefined ? uiEvent.escapeDescription : true;
    monthTable._escapeEventResources = uiEvent.escapeResource !== undefined ? uiEvent.escapeResource : true;

    var weekdayHeadersRowSeparator = stylingParams.weekdayHeadersRowSeparator ? stylingParams.weekdayHeadersRowSeparator : "1px solid gray";
    var weekdayColumnSeparator = stylingParams.weekdayColumnSeparator ? stylingParams.weekdayColumnSeparator : "1px solid silver";
    var rowSeparator = stylingParams.rowSeparator ? stylingParams.rowSeparator : "1px solid #b0b0b0";
    var secondaryRowSeparator = stylingParams.secondaryRowSeparator ? stylingParams.secondaryRowSeparator : "1px solid #e8e8e8";
    var columnSeparator = stylingParams.columnSeparator ? stylingParams.columnSeparator : "1px solid #b0b0b0";
    var dayHeaderRowClass = O$.combineClassNames(["o_monthTableDayHeaderRow", stylingParams.dayHeaderRowClass]);
    var timetableViewRowClass = O$.combineClassNames(["o_monthTableRow", stylingParams.rowClass]);

    var weekdayHeaderCellClass = O$.combineClassNames(["o_monthTableWeekdayHeaderCell", stylingParams.weekdayHeaderCellClass]);
    var weekendWeekdayHeaderCellClass = O$.combineClassNames([weekdayHeaderCellClass, "o_monthTableWeekendWeekdayHeaderCell", stylingParams.weekendWeekdayHeaderCellClass]);
    var cellHeaderClass = O$.combineClassNames(["o_monthTableCellHeader", stylingParams.cellHeaderClass]);
    var cellClass = O$.combineClassNames(["o_monthTableCell", stylingParams.cellClass]);
    var todayCellHeaderClass = O$.combineClassNames([/*cellHeaderClass,*/ "o_monthTableTodayCellHeader", stylingParams.todayCellHeaderClass]);
    var todayCellClass = O$.combineClassNames([/*cellClass,*/ "o_monthTableTodayCell", stylingParams.todayCellClass]);
    var weekendCellHeaderClass = O$.combineClassNames([/*cellHeaderClass,*/ "o_monthTableWeekendCellHeader", stylingParams.weekendCellHeaderClass]);
    var weekendCellClass = O$.combineClassNames([/*cellClass,*/ "o_monthTableWeekendCell", stylingParams.weekendCellClass]);
    var inactiveMonthCellHeaderClass = O$.combineClassNames([/*cellHeaderClass,*/ "o_monthTableInactiveMonthCellHeader", stylingParams.inactiveMonthCellHeaderClass]);
    var inactiveMonthCellClass = O$.combineClassNames([/*cellClass,*/ "o_monthTableInactiveMonthCell", stylingParams.inactiveMonthCellClass]);
    monthTable._weekdayHeaderCellClass = weekdayHeaderCellClass;
    monthTable._weekendWeekdayHeaderCellClass = weekendWeekdayHeaderCellClass;
    monthTable._cellHeaderClass = cellHeaderClass;
    monthTable._cellClass = cellClass;
    monthTable._todayCellHeaderClass = todayCellHeaderClass;
    monthTable._todayCellClass = todayCellClass;
    monthTable._weekendCellHeaderClass = weekendCellHeaderClass;
    monthTable._weekendCellClass = weekendCellClass;
    monthTable._inactiveMonthCellHeaderClass = inactiveMonthCellHeaderClass;
    monthTable._inactiveMonthCellClass = inactiveMonthCellClass;

    var eventElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "height"));
    var eventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "marginLeft"));
    var eventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "marginRight"));
    var reservedEventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._reservedTimeEventClass, "marginLeft"));
    var reservedEventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._reservedTimeEventClass, "marginRight"));
    var moreLinkElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(moreLinkElementClass, "height"));

    var weekdayHeadersTable = O$(componentId + "::weekdayHeaders");

    var firstDayOfWeek = (calendarOptions && calendarOptions.firstDayOfWeek) ? calendarOptions.firstDayOfWeek : 0;

    var columns = [];
    var weekdayHeaderColumns = [];

    for (var weekDay = 0; weekDay < 7; weekDay++) {
      columns.push({});
      weekdayHeaderColumns.push({});
    }
    weekdayHeaderColumns.push({className: "o_defaultScrollBarWidth"});

    if (monthTable._useResourceSeparation) {
      monthTable._resourcesByIds = {};
      monthTable._idsByResourceNames = {};
      resources.forEach(function(resource) {
        monthTable._resourcesByIds[resource.id] = resource;
        monthTable._idsByResourceNames[resource.name] = resource.id;
      });
    }

    var forceUsingCellStyles = true; // allow such styles as text-align to be applied to row's cells

    O$.Tables._init(monthTable._table, {
              columns: columns,
              gridLines: [rowSeparator, columnSeparator, null, null, null, null, null, null, null, null, null],
              forceUsingCellStyles: forceUsingCellStyles
            });

    O$.Tables._init(weekdayHeadersTable, {
              columns: weekdayHeaderColumns,
              gridLines: [rowSeparator, weekdayColumnSeparator, null, null, null, null, null, null, null, null, null],
              body: {rowClassName: weekdayHeadersRowClass},
              forceUsingCellStyles: forceUsingCellStyles
            });
    weekdayHeadersTable.style.borderBottom = weekdayHeadersRowSeparator;
    weekdayHeadersTable.body._overrideVerticalGridline(weekdayHeaderColumns.length - 2, O$.isExplorer6() ? "1px solid white" : "1px solid transparent");

    for (var week = 0; week < 6; week++) {
      monthTable._table.body._newRow().className = dayHeaderRowClass; // day header row
      monthTable._table.body._newRow().className = timetableViewRowClass; // day content row
    }

    var rows = monthTable._table.body._getRows();
    rows.forEach(function(row) {
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        cell.onclick = function() {
          // onclick event can be fired on drag end under IE
          if (editable) {
            var newEventTime = this._cellDay;
            monthTable._addEvent(newEventTime, this._resource ? this._resource.id : null);
          }
        };
      }
      cells[cells.length - 1]._last = true;
    });

    // workaround to show *horizontal* gridlines
    monthTable._table.body._overrideVerticalGridline(0, columnSeparator);

    var rowCount = monthTable._table.body._getRows().length;
    monthTable._table.body._getBorderBottomForCell = function(rowIndex, colIndex, cell) {
      if (rowIndex == rowCount - 1) return "none";
      if (rowIndex % 2 == 0)
        return secondaryRowSeparator;
      else
        return rowSeparator;
    };

    monthTable._table.body._updateVerticalGridlines();

    function clearAllCellEvents() {
      var rows = monthTable._table.body._getRows();
      for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
        var row = rows[rowIndex];
        var cells = row._cells;
        for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
          var cell = cells[cellIndex];
          if (cell._cellEvents) {
            cell._cellEvents.forEach(function(oldCellEvent) {
              oldCellEvent._removeElements();
            });
          }
          cell._cellEvents = [];
        }
      }
    }

    monthTable._updateCellEventElements = function(day) {
      var cell = O$.MonthTable.getCellForDay(this, day);

      if (!cell) {
        return;
      }

      if (cell._cellEvents) {
        cell._cellEvents.forEach(function(oldCellEvent) {
          oldCellEvent._removeElements();
        });
      }

      cell._cellEvents = [];

      var cellEvents = O$.MonthTable.getDayEvents(this._events, day);

      for (var cellEventIndex = 0; cellEventIndex < cellEvents.length; cellEventIndex++) {
        var cellEvent = cellEvents[cellEventIndex];
        cellEvent._cell = cell;
        cellEvent._cellEventIndex = cellEventIndex;
        cell._cellEvents.push(cellEvent);
      }

      cell._moreLinkData = null;

      cellEvents.forEach(function(cellEvent) {
        monthTable._addEventElements(cellEvent);
      });
      cell._moreLinkElement._update();
      cell._moreLinkData = null;

    };

    function addMoreLink(cell) {
      var moreLinkElement = document.createElement("div");
      moreLinkElement.className = moreLinkElementClass;
      moreLinkElement.style.zIndex = monthTable._baseZIndex + 5;
      moreLinkElement._cell = cell;
      cell._moreLinkElement = moreLinkElement;
      var link = document.createElement("a");
      link.className = moreLinkClass;
      link.setAttribute("href", "javascript:");
      O$.setInnerText(link, moreLinkText);
      moreLinkElement.appendChild(link);

      link.onmousedown = O$.stopEvent;
      link.onclick = O$.stopEvent;

      moreLinkElement._updatePos = function() {
        if (!cell._moreLinkData) {
          this.style.display = "none";
          return;
        }

        this.style.display = "";
        var cellBoundaries = O$.getElementBorderRectangle(cell, true);

        var topY = cell._moreLinkData.topY;
        var bottomY = topY + moreLinkElementHeight;

        var x1 = cellBoundaries.getMinX();
        var x2 = cellBoundaries.getMaxX();
        if (O$.isExplorer() && O$.isStrictMode() && cell._last) {
          var scroller = O$(monthTable.id + "::scroller");
          var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
          x2 -= scrollerWidth;
        }

        var rect = new O$.Rectangle(Math.round(x1), Math.round(topY),
                Math.round(x2 - x1), Math.round(bottomY - topY));
        this._rect = rect;

        O$.setElementBorderRectangle(moreLinkElement, moreLinkElement._rect);
      };

      moreLinkElement._update = function() {
        this._updatePos();
      };

      monthTable._absoluteElementsParentNode.appendChild(moreLinkElement);
      return moreLinkElement;
    }

    for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
      var row = rows[rowIndex];
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        addMoreLink(cell);
      }
    }

    var super_addEventElement = monthTable._addEventElement;
    var super_removeEventElement = monthTable._removeEventElement;
    var super_removeEventElements = monthTable._removeEventElements;
    O$.extend(monthTable, {
              _getEventEditor: function() {
                if (!editable)
                  return null;
                return this._eventEditor;
              },


              _addEventElement: function(event, part) {
                var eventElement = super_addEventElement.call(this, event, part);

                event._updateRolloverState = function() {

                  var mouseInsideEventElements = false;
                  for (var i = 0; i < event.parts.length; i++) {
                    var eventElement = event.parts[i].mainElement;
                    if (!eventElement) {
                      // this can be the case because _updateRolloverState is invoked by time-out, so if mouseOver/mouseOut happens
                      // just before element is replaced with Ajax, this call will be made when there's no original element anymore
                      return;
                    }
                    mouseInsideEventElements |= eventElement._mouseInside;
                  }

                  var actionBar = monthTable._getEventActionBar();
                  event._setMouseInside(mouseInsideEventElements
                          || (actionBar._event == event && actionBar._actionsArea._mouseInside)
                  );
                };

                eventElement._update = function() {
                  this._updatePos();
                  this._updateShape();
                };
                eventElement._updatePos = function() {
                  if (event.resourceId) {
                    var resource = monthTable._getResourceForEvent(event);
                    if (!resource) {
                      this.style.display = "none";
                      return;
                    }
                  }
                  this.style.display = "";

                  var cell = event._cell;
                  var cellEventIndex = event._cellEventIndex;
                  var cellBoundaries = O$.getElementBorderRectangle(cell, true);

                  var topY = cellBoundaries.getMinY() + eventElementHeight * cellEventIndex;
                  var bottomY = topY + eventElementHeight;

                  var lastForCell = (cellEventIndex == cell._cellEvents.length - 1);
                  var maxY = lastForCell ? cellBoundaries.getMaxY() : cellBoundaries.getMaxY() - moreLinkElementHeight;

                  if (cell._moreLinkData) {
                    this.style.display = "none";
                    return;
                  }

                  if (bottomY > maxY) {
                    cell._moreLinkData = { topY: topY };
                    this.style.display = "none";
                    return;
                  }

                  var x1 = cellBoundaries.getMinX() + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
                  var x2 = cellBoundaries.getMaxX() - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);
                  if (O$.isExplorer() && O$.isStrictMode() && cell._last) {
                    var scroller = O$(monthTable.id + "::scroller");
                    var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
                    x2 -= scrollerWidth;
                  }
                  var rect = new O$.Rectangle(Math.round(x1), Math.round(topY),
                          Math.round(x2 - x1), Math.round(bottomY - topY));
                  this._rect = rect;
                  var backgroundElement = this._backgroundElement;

                  O$.setElementBorderRectangle(eventElement, eventElement._rect);
                  O$.setElementBorderRectangle(backgroundElement, eventElement._rect);
                };

                //TODO: move upper
                event._updateAreaPositionsAndBorder = function() {
                  event._updateAreaPositions(true);
                };

                event._isEventPreviewAllowed = function() {
                  return event._mouseInside;
                };

                event._isEventUpdateNotAllowed = function() {
                  return event.type == "reserved";
                };

                event._beforeUpdate = function() {
                  event._oldStart = event.start;
                };

                eventElement._update();
                return eventElement;
              },

              _removeEventElement: function(event, part) {
                if (!part.mainElement)
                  return;

                super_removeEventElement.call(this, event, part);
              },


              _layoutActionBar: function(actionBar, barHeight, eventElement) {
                var barWidth = actionBar._actionsArea._getWidth();
                O$.setElementSize(actionBar, {width: barWidth, height: barHeight});
                actionBar.style.left = "";
                actionBar.style.bottom = "";
                actionBar.style.right = "0px";
                actionBar.style.top = (eventElement._rect.height - barHeight) / 2 + "px";
              },


              _updateStartEndTime: function() {

                this._startTime = O$.MonthTable.getDay(this._day, firstDayOfWeek);
                this._endTime = O$.MonthTable.getFirstDayOut(this._day, firstDayOfWeek);

                O$.MonthTable.updateCellDays(this);

              },

              _updateTimeForCells: function() {
                var weekdayHeaderRow = weekdayHeadersTable.body._getRows()[0];

                var weekdayHeaderCellDay = this._startTime;
                for (var i = 0; i < weekdayHeaderRow._cells.length - 1; i++) {
                  var weekdayHeaderCell = weekdayHeaderRow._cells[i];
                  var weekdayHeaderCellClassName = this._weekdayHeaderCellClass;
                  if (weekdayHeaderCellDay.getDay() == 0 || weekdayHeaderCellDay.getDay() == 6) {
                    weekdayHeaderCellClassName = this._weekendWeekdayHeaderCellClass;
                  }
                  weekdayHeaderCell.className = weekdayHeaderCellClassName;
                  O$.removeAllChildNodes(weekdayHeaderCell);
                  var weekdayHeaderCellText = dtf.format(weekdayHeaderCellDay, weekdayPattern);
                  var weekdayHeaderCellStyledText = O$.createStyledText(weekdayHeaderCellText, weekdayStyle);
                  weekdayHeaderCell.appendChild(weekdayHeaderCellStyledText);
                  weekdayHeaderCellDay = O$.incDay(weekdayHeaderCellDay);
                }

              },

              _removeEventElements: function() {
                super_removeEventElements.call(this);
                clearAllCellEvents();
              },

              _updateEventElements: function(reacquireDayEvents, refreshAreasAfterReload) {
                if (!this._isActive()) return;
                this._baseZIndex = O$.getElementZIndex(this);
                this._removeEventElements();
                if (reacquireDayEvents)
                  this._events = this._eventProvider._getEventsForPeriod(this._startTime, this._endTime, function() {
                    this._updateEventElements(true, true);
                  });

                for (var cellDate = this._startTime; cellDate < this._endTime; cellDate = O$.MonthTable.__incDay(cellDate)) {
                  this._updateCellEventElements(cellDate);
                }

                if (refreshAreasAfterReload) {
                  this._events.forEach(function(event) {
                    event._attachAreas();
                  });
                }

                this._updateEventZIndexes();
              },

              previousMonth: function() {
                var prevDay = new Date(this._day.getFullYear(), this._day.getMonth() - 1, this._day.getDate());
                this.setDay(prevDay);
              },

              nextMonth: function() {
                var nextDay = new Date(this._day.getFullYear(), this._day.getMonth() + 1, this._day.getDate());
                if ((nextDay.getMonth() - this._day.getMonth()) % 12 != 1) {
                  nextDay = new Date(this._day.getFullYear(), this._day.getMonth() + 2, 0);
                }
                this.setDay(nextDay);
              },

              currentMonth: function() {
                var today = new Date();
                this.setDay(today);
              },

              _updateEventsPresentation: function() {
                var rows = monthTable._table.body._getRows();
                for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
                  var row = rows[rowIndex];
                  var cells = row._cells;
                  for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
                    var cell = cells[cellIndex];
                    cell._moreLinkData = null;
                    cell._cellEvents.forEach(function(event) {
                      event.updatePresentation();
                    });
                    if (cell._moreLinkElement) {
                      cell._moreLinkElement._update();
                    }
                    cell._moreLinkData = null;
                  }
                }

              },

              _accountForScrollerWidth: function() {
                var firstDataRow = this._table.body._getRows()[0];
                var visibleDataWidth = O$.getElementBorderRectangle(firstDataRow._cells[firstDataRow._cells.length - 1]).getMaxX() - O$.getElementPos(firstDataRow._cells[0]).x;

                var weekdayHeaderRow = weekdayHeadersTable.body._getRows()[0];
                var totalWeekdayHeaderWidth = O$.getElementBorderRectangle(weekdayHeaderRow._cells[weekdayHeaderRow._cells.length - 1]).getMaxX() - O$.getElementPos(weekdayHeaderRow._cells[0]).x;
                var lastHeaderColWidth = totalWeekdayHeaderWidth - visibleDataWidth;
                if (lastHeaderColWidth < 0) // can be the case under Mozilla3 because O$.getElementBorderRectangle returns non-rounded coordinates, which may result in values like -0.2499...
                  lastHeaderColWidth = 0;
                if (lastHeaderColWidth == 0 && O$.isOpera())
                  lastHeaderColWidth = 1;
                O$.setElementWidth(weekdayHeaderRow._cells[weekdayHeaderRow._cells.length - 1], lastHeaderColWidth);
              }

            });

    monthTable._adjustRolloverPaddings();

    var dtf = O$.getDateTimeFormatObject(locale);
    monthTable.setDay(dtf.parse(day, "dd/MM/yyyy"));

    monthTable._updateHeightForFF();

    monthTable._putTimetableChanges(null, null, null, true);
    O$.assignEvents(monthTable, {onchange: onchange}, true);

    O$.addInternalLoadEvent(function() {
      var maxScrollOffset = monthTable._scroller.scrollHeight - O$.getElementSize(monthTable._scroller).height;
      if (maxScrollOffset < 0)
        maxScrollOffset = 0;
      if (scrollOffset > maxScrollOffset)
        scrollOffset = maxScrollOffset;
      monthTable._scroller.scrollTop = scrollOffset;
      O$.addEventHandler(monthTable._scroller, "scroll", function() {
        O$.setHiddenField(monthTable, monthTable.id + "::scrollPos", monthTable._scroller.scrollTop);
      });

      monthTable.updateLayout(); // update positions after layout changes that might have had place during loading
    });

    O$.addEventHandler(window, "resize", function() {
      monthTable.updateLayout();
    });
  },

  getDay: function(day, firstDayOfWeek) {
    var firstDayOfMonth = O$.cloneDate(day);
    firstDayOfMonth.setDate(1);

    var dayOfWeek = firstDayOfMonth.getDay();
    var decrement = dayOfWeek - firstDayOfWeek;
    if (decrement < 0) {
      decrement += 7;
    }
    var result = O$.MonthTable.__incDay(firstDayOfMonth, -decrement);
    return result;
  },

  getFirstDayOut: function(day, firstDayOfWeek) {
    var lastDayOfMonth = new Date(day.getFullYear(), day.getMonth() + 1, 0);
    var lastDayOfMonthWeekday = lastDayOfMonth.getDay();
    var increment = firstDayOfWeek + 7 - lastDayOfMonthWeekday;
    var result = new Date(lastDayOfMonth.getFullYear(), lastDayOfMonth.getMonth(), lastDayOfMonth.getDate() + increment);
    return result;
  },

  getNextMonthStart: function(date) {
    return new Date(date.getFullYear(), date.getMonth() + 1, 1);
  },


  getDayEvents: function(allEvents, day) {
    var result = [];
    allEvents.forEach(function(currEvent) {
      if (currEvent.type != "reserved") {
        var start = currEvent.start;
        if (O$._datesEqual(start, day)) {
          result.push(currEvent);
        }
      }
    });
    result.sort(O$.Timetable.compareEventsByStart);
    return result;
  },

  updateCellDays: function(monthTable) {
    var numberOfWeeks = Math.round((monthTable._endTime - monthTable._startTime) / (7 * 24 * 60 * 60 * 1000));
    var visibleRowCount = 2 * numberOfWeeks;
    O$.MonthTable.setVisibleRowCount(monthTable._table, visibleRowCount);

    var cellDay = monthTable._startTime;
    var rows = monthTable._table.body._getRows();
    for (var rowIndex = 0; rowIndex < visibleRowCount; rowIndex += 2) {
      var dayHeaderRow = rows[rowIndex];
      var dayContentRow = rows[rowIndex + 1];
      var dayHeaderCells = dayHeaderRow._cells;
      var dayContentCells = dayContentRow._cells;
      var cellCount = dayHeaderCells.length;

      var today = new Date();

      for (var cellIndex = 0; cellIndex < cellCount; cellIndex++) {
        var dayHeaderCell = dayHeaderCells[cellIndex];
        var dayContentCell = dayContentCells[cellIndex];

        var cellHeaderClasses = [ monthTable._cellHeaderClass ];
        var cellClasses = [ monthTable._cellClass ];

        if (cellDay.getDay() == 0 || cellDay.getDay() == 6) {
          cellHeaderClasses.push(monthTable._weekendCellHeaderClass);
          cellClasses.push(monthTable._weekendCellClass);
        }

        if (cellDay.getMonth() != monthTable._day.getMonth()) {
          cellHeaderClasses.push(monthTable._inactiveMonthCellHeaderClass);
          cellClasses.push(monthTable._inactiveMonthCellClass);
        }

        if (O$._datesEqual(cellDay, today)) {
          cellHeaderClasses.push(monthTable._todayCellHeaderClass);
          cellClasses.push(monthTable._todayCellClass);
        }

        var cellHeaderClass = O$.combineClassNames(cellHeaderClasses);
        var cellClass = O$.combineClassNames(cellClasses);

        dayHeaderCell.className = cellHeaderClass;
        dayContentCell.className = cellClass;
        dayHeaderCell._cellDay = cellDay;
        dayContentCell._cellDay = cellDay;
        O$.removeAllChildNodes(dayHeaderCell);
        var text = document.createTextNode(cellDay.getDate());
        dayHeaderCell.appendChild(text);
        cellDay = O$.MonthTable.__incDay(cellDay);
      }
    }
  },

  getCellForDay: function(monthTable, day) {
    var rows = monthTable._table.body._getRows();
    for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
      var row = rows[rowIndex];
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        var cellDay = cell._cellDay;
        if (O$._datesEqual(cellDay, day)) {
          return cell;
        }
      }
    }
    return null;
  },

  // increment the date without setting time to 12:00
  __incDay: function(date, increment) {
    var newDate = O$.cloneDateTime(date);
    if (increment === 0) {
      return newDate;
    }
    if (!increment) {
      increment = 1;
    }
    newDate.setDate(newDate.getDate() + increment);
    return newDate;
  },


  setVisibleRowCount: function(table, count) {
    var rows = table.body._getRows();
    var rowCount = Math.min(count, rows.length);
    for (var visibleRowIndex = 0; visibleRowIndex < rowCount; visibleRowIndex++) {
      rows[visibleRowIndex].style.display = "";
    }
    for (var hiddenRowIndex = rowCount; hiddenRowIndex < rows.length; hiddenRowIndex++) {
      rows[hiddenRowIndex].style.display = "none";
    }
  }

};


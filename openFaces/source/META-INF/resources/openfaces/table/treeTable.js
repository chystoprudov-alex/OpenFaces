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

// -------------------------- INITIALIZATION

O$.TreeTable = {
  _initTreeTableAPI: function(table) {
    O$.extend(table, {
      _of_treeTableComponentMarker: true,
      __getSelectedNodeCount: function () {
        var selectedItems = this._getSelectedItems();
        if (!selectedItems || (selectedItems.length == 1 && selectedItems[0] == -1))
          return 0;
        return selectedItems.length;
      },
      __setSelectedNodeIndexes: function (nodeIndexes) {
        this.__setSelectedRowIndexes(nodeIndexes);
      },
      __setSelectedNodeIndex: function (nodeIndex) {
        this.__setSelectedRowIndex(nodeIndex);
      },
      clearSelection: function() {
        this.__clearSelection();
      },
      isSelectionEmpty: function() {
        return this.__isSelectionEmpty();
      },
      getSelectedNodeCount: function() {
        return this.__getSelectedNodeCount();
      },
      getSelectedNodeKey: function() {
        return this.__getSelectedRowKey();
      },
      setSelectedNodeKey: function(rowKey) {
        this.__setSelectedRowKey(rowKey);
      },
      getSelectedNodeKeys: function() {
        return this.__getSelectedRowKeys();
      },
      setSelectedNodeKeys: function(rowKey) {
        this.__setSelectedRowKeys(rowKey);
      }

    });
  },

  // -------------------------- FOLDING SUPPORT

  _initFolding: function(tableId, toggleClassName, clientFoldingParams, structureImageUrl) {
    var rowIndexToChildCount = clientFoldingParams ? clientFoldingParams[0] : null;
    var treeColumnParams = clientFoldingParams ? clientFoldingParams[1] : null;

    var table = O$.initComponent(tableId, null, {
      _rowIndexToChildCount: rowIndexToChildCount,
      _foldingMode: clientFoldingParams ? "client" : "server",
      _treeColumnParams: treeColumnParams,
      _toggleClassName: toggleClassName,
      _structureImageUrl: structureImageUrl,

      _updateRowVisibility: function() {
        var rowIndexToChildCount = this._rowIndexToChildCount;
        var rootNodeCount = rowIndexToChildCount["root"];
        var rows = table.body._getRows();
        this._styleRecalculationOnNodeExpansionNeeded = !!this._params.body.oddRowClassName;
        var pseudoCommonRootRow = {_pseudoRow: true, _childRows: []};

        function processRowVisibility(rows, rowIndexToChildCount, rowIndex, rowCount, currentLevelVisible, level, parentRow) {
          for (var i = 0; i < rowCount; i++) {
            var row = rows[rowIndex];
            O$.assert(row, "processRowVisibility: rowIndex == " + rowIndex);
            row._level = level;
            row._setVisible(currentLevelVisible);
            if (!row._table._styleRecalculationOnNodeExpansionNeeded) {
              if (currentLevelVisible && O$.isExplorer()) { // workaround for IE issue: when custom row style is applied to
                            // parent rows and a node is expanded, spacing between cells becomes white after node expansion
                row._updateStyle();
              }
            }
            row._parentRow = !parentRow._pseudoRow ? parentRow : null;
            if (parentRow) {
              parentRow._childRows.push(row);
            }

            var childCount = rowIndexToChildCount[rowIndex];
            if (childCount) {
              row._hasChildren = true;
              row._childRows = [];
              row._childrenLoaded = (childCount != "?");
              var nextLevelVisible = currentLevelVisible && row._isExpanded();
              if (row._childrenLoaded) {
                rowIndex = processRowVisibility(rows, rowIndexToChildCount, rowIndex + 1, childCount, nextLevelVisible, level + 1, row);
              } else
                rowIndex++;
            } else {
              row._hasChildren = false;
              rowIndex++;
            }
          }
          return rowIndex;
        }
        processRowVisibility(rows, rowIndexToChildCount, 0, rootNodeCount, true, 0, pseudoCommonRootRow);

        table._rootRows = pseudoCommonRootRow._childRows;

        if (this._styleRecalculationOnNodeExpansionNeeded) {
          var visibleRows = 0;
          rows.forEach(function(row) {
            if (row._isVisible()) row._notifyRowMoved(visibleRows++);
          });
          rows.forEach(function(row) {
            if (row._isVisible()) {
              // _updateStyle is used for two reasons:
              // - show selection style if selected row was previously in an invisible branch
              // - workaround for IE issue: when custom row style is applied to parent rows and a node is expanded, spacing between cells becomes white after node expansion
              row._updateStyle();
            }
          });

        }
        O$.invokeFunctionAfterDelay(function() {
//          table._alignRowHeights();
          if (table._synchronizeVerticalAreaScrolling)
            table._synchronizeVerticalAreaScrolling();
        }, 50);
      },

      _updateExpandedNodesField: function() {
        var field = O$(this.id + "::expandedNodes");
        var expandedRowIndexes = "";
        var rows = this.body._getRows();
        for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
          var row = rows[rowIndex];
          var expanded = row._isExpanded();
          if (!expanded) continue;
          if (expandedRowIndexes.length > 0)
            expandedRowIndexes += ",";
          expandedRowIndexes += rowIndex;
        }
        field.value = expandedRowIndexes;
      },

      _onKeyboardNavigation: function(e) {
        var selectedItems = this._getSelectedItems();
        if (selectedItems.length != 1)
          return true;
        var selectedRowIndex = selectedItems[0];
        if (selectedRowIndex == -1)
          return true;
        var bodyRows = this.body._getRows();
        var row = bodyRows[selectedRowIndex];
        if (!row._hasChildren)
          return true;
        if ((e.rightPressed || e.plusPressed) && !row._isExpanded()) {
          row._setExpanded(true);
          return false;
        }
        if ((e.leftPressed || e.minusPressed) && row._isExpanded()) {
          row._setExpanded(false);
          return false;
        }

        return true;
      }

    });

    var super_addLoadedRows = table._addLoadedRows;
    table._addLoadedRows = function(subRowsData) {
      super_addLoadedRows.apply(this, arguments);

      var newRows = this.__newRows;
      var parentRowIndex = this.__afterRowIndex;
      var newRowIndexToChildCount = subRowsData["structureMap"];

      var rows = this.body._getRows();
      var parentRow = parentRowIndex != -1 ? rows[parentRowIndex] : null;

      if (parentRow) {
        if (newRows == null || newRows.length == 0) {
          parentRow._childrenEmpty = true;
          var toggles = parentRow._toggles;
          toggles.forEach(function(toggle) {
            toggle.style.visibility = "hidden";
            toggle.className = "";
          });
        }
        parentRow._childrenLoaded = true;
      }
      var addedRowCount = newRows.length;
      var i;
      for (i = 0; i < addedRowCount; i++) {
        var newRow = newRows[i];
        O$.TreeTable._initRow(newRow);
      }

      if (parentRowIndex == -1)
        this._rowIndexToChildCount = newRowIndexToChildCount;
      else {
        var rowIndexToChildCount = this._rowIndexToChildCount;
        var newRowIndex;
        for (var rowIndex = rows.length - 1 - addedRowCount; rowIndex > parentRowIndex; rowIndex--) {
          if (rowIndexToChildCount[rowIndex] != undefined) {
            newRowIndex = rowIndex + addedRowCount;
            rowIndexToChildCount[newRowIndex] = rowIndexToChildCount[rowIndex];
            rowIndexToChildCount[rowIndex] = undefined;
          }
        }
        rowIndexToChildCount[parentRowIndex] = newRowIndexToChildCount[0];
        for (i = 0; i < addedRowCount; i++) {
          newRowIndex = parentRowIndex + 1 + i;
          rowIndexToChildCount[newRowIndex] = newRowIndexToChildCount[i + 1];
        }
      }

      this._updateExpandedNodesField();
      if (table._selectionMode == "hierarchical") {
        // _updateRowVisibility is needed to update parent/child references, but we can do it asynchronously to avoid
        // degrading the perceived performance
        setTimeout(function() {
          table._updateRowVisibility();
        }, 1);
      }
    };

    var rows = table.body._getRows();
    rows.forEach(O$.TreeTable._initRow);
    if (table._foldingMode == "client") {
      table._updateRowVisibility();
      table._updateExpandedNodesField();
    }

  },

  _initRow: function(row) {
    var table = row._table;

    var expandedToggles = [];
    var collapsedToggles = [];
    [row._leftRowNode, row._rowNode, row._rightRowNode].forEach(function(n) {
      if (!n) return;
      expandedToggles = expandedToggles.concat(O$.getChildNodesByClass(n, "o_toggle_e"));
      collapsedToggles = collapsedToggles.concat(O$.getChildNodesByClass(n, "o_toggle_c"));
    });
    var expanded = expandedToggles && expandedToggles.length > 0;
    O$.assert(expandedToggles.length == 0 || collapsedToggles.length == 0,
            "A row can't contain both expanded and collapsed nodes. row._index = " + row._index +
            "; table id = " + row._table.id);
    var toggles = expanded ? expandedToggles : collapsedToggles;

    var hasChildren = toggles && toggles.length > 0;
    function updateExpansionStateClass() {
      O$.setStyleMappings(row._rowNode, {expansion: row._isExpanded() ? "o_expandedNode" : "o_collapsedNode"});
    }

    var super_updateStyle = row._updateStyle;
    O$.extend(row, {
      _toggles: toggles,
      _hasChildren: hasChildren,
      _expanded: hasChildren && expanded,

      _updateStructureLine: function() {
        if (false) { //table._structureImageUrl) {
//          var d = document.createElement("div");
//          d.style.position = "absolute";
//          d.style.border = "1px solid red";
//          d.style.left = "0px";
//          d.style.top = "0px";

          var treeCell = row._cells[0];
//          treeCell.appendChild(d);
          var subCells = treeCell.getElementsByTagName("td");

          var level = subCells.length - 2;
          var bkgnd = "";
          var bkgndUrls = "";
          var bkgndPositions = "";
          for (var l = 0; l <= level; l++) {
            if (bkgnd.length > 0) bkgnd += ", ";
            if (bkgndUrls.length > 0) bkgndUrls += ", ";
            if (bkgndPositions.length > 0) bkgndPositions += ", ";
            var structureCell = subCells[l];
            var leftPos = O$.getElementBorderRectangle(structureCell, treeCell).x;
            bkgnd += "url(" + table._structureImageUrl + ") no-repeat " + leftPos + "px center"
          }
          treeCell.style.background = bkgnd;
//          treeCell.style.backgroundUrl = bkgndUrls;
//          treeCell.style.backgroundPosition = bkgndPositions;
        }
      },

      _updateStyle: function() {
        super_updateStyle.apply(this, arguments);
        this._updateStructureLine();
      },

      /*
       _setExpanded(expanded) may not be able to set the "expanded" state to true in some cases.
       Invokers must check _isExpanded() to see if expansion was successful.
       */
      _setExpanded: function(expanded) {
        if (row._childrenEmpty)
          return;
        if (this._expanded == expanded)
          return;
        var rowTable = this._table;
        var prevExpanded = this._expanded;
        this._expanded = expanded;
        var thisRow = this;
        updateExpansionStateClass();

        function changeToggleImage(showExpandedImage) {
          var toggles = thisRow._toggles;
          for (var toggleIndex = 0, toggleCount = toggles.length; toggleIndex < toggleCount; toggleIndex++) {
            var toggle = toggles[toggleIndex];
            var treeColumnParams = rowTable._treeColumnParams[toggleIndex];
            toggle.src = showExpandedImage ? treeColumnParams[0] : treeColumnParams[1];
          }
        }

        changeToggleImage(expanded);
        rowTable._updateExpandedNodesField();
        if (expanded && !this._childrenLoaded) {
          if (rowTable._useAjax) {
            var ajaxFailedProcessor = function() {
              thisRow._expanded = prevExpanded;
              changeToggleImage(prevExpanded);
              rowTable._updateExpandedNodesField();
            };
            if (O$._ajaxRequestScheduled || O$._ajax_request_processing)
              ajaxFailedProcessor();
            else
              O$.requestComponentPortions(rowTable.id, ["subRows:" + this._index], null, O$.Table._acceptLoadedRows, ajaxFailedProcessor);
          } else
            O$.submitWithParam(rowTable, rowTable.id + "::toggleExpansion", this._index);
        } else {
          rowTable._updateRowVisibility();
        }
      },
      _isExpanded: function() {
        return this._expanded;
      }
    });

    updateExpansionStateClass();

    toggles.forEach(function(toggle) {
      O$.extend(toggle, {
        className: table._toggleClassName,
        _row: row,
        onclick: function(e) {
          var evt = O$.getEvent(e);
          var clickedRow = this._row;
          var newExpanded = !clickedRow._isExpanded();
          clickedRow._setExpanded(newExpanded);
          if (table._focusable) {
            table._preventPageScrolling = true;
            table.focus();
            table._preventPageScrolling = false;
          }
          O$.cancelEvent(e);
        },
        ondblclick: function(e) {
          O$.repeatClickOnDblclick.apply(this, [e]);
          O$.cancelEvent(e);
        }
      });
    });

    row._updateStructureLine();
  },

  _setSelectedNodeIndexes: function(treeTableId, selectedNodeIndexes) {
    if (!treeTableId)
      throw "O$._setSelectedNodeIndexes: treeTable's clientId must be passed as a parameter";
    var table = O$(treeTableId);
    if (!table)
      throw "O$._setSelectedNodeIndexes: Invalid clientId passed - no such component was found: " + treeTableId;
    if (!table._of_treeTableComponentMarker)
      throw "O$._setSelectedNodeIndexes: The clientId passed refers to a component other than <o:treeTable> : " + treeTableId;
    if (!table._multipleSelectionAllowed)
      throw "O$._setSelectedNodeIndexes: The table is not set up for multiple selection. Table's clientId is: " + treeTableId;
    if (!selectedNodeIndexes) {
      throw "O$._setSelectedNodeIndexes: Invalid selectedNodeIndexes passed : " + treeTableId;
    }
    var selectedIndexes = [];
    selectedNodeIndexes.forEach(function(idx) {
      if (idx > -1) selectedIndexes.push(idx);
    });
    table._setSelectedItems(selectedIndexes, true);
  }

};
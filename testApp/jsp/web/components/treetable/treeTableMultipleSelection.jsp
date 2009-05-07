<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>Multiple Selection in the TreeTable</title>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
  <script type="text/javascript" src="../../funcTestsUtil.js"></script>
</head>

<body onload="multipleSelectionChanged('formID:multipleNodePathsSelectionTreeTableID', 'selectionNodePathsID');
multipleSelectionChanged('formID:multipleNodeDatasSelectionTreeTableID', 'selectionNodeDatasID');">

<f:view>
  <h:form id="formID">
   <%@ include file="treeTableMultipleSelection_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
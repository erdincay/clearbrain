<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta name="decorator" content="logged" />
	<title>
		<c:choose>
			<c:when test="${typeDashboard == 'TODAY'}">
				<spring:message code="dashboard.title.today" />
			</c:when>
			<c:when test="${typeDashboard == 'TOMORROW'}">
				<spring:message code="dashboard.title.tomorrow" />
			</c:when>
			<c:when test="${typeDashboard == 'THIS_WEEK'}">
				<spring:message code="dashboard.title.week" />
			</c:when>
			<c:otherwise>
				<spring:message code="dashboard.title.global" />
			</c:otherwise>
		</c:choose>
	</title>
	<script type="text/javascript" src="<spring:url value="/js/logged/dashboard.js" />" ></script>
	<script type="text/javascript" src="<spring:url value="/js/logged/todo.js" />" ></script>

	<%-- Create an array to keep links between a note and a category --%>
	<script type="text/javascript">
		<c:forEach var="entry" items="${notesCatIds}">catNA[${entry.key}] = ${entry.value};</c:forEach>
	</script>
</head>

<body>
	<%-- Left --%>
	<div class="span-7">
		<%-- Date --%>
		<div class="top-frames date-container">
			<spring:message code="header.dateFormat" var="dateFormat" />
			<c:set var="today" value="<%=new java.util.Date()%>" />
			<fmt:formatDate value="${today}" pattern="${dateFormat}" />
		</div><br /><br />

		<%-- Categories --%>
		<div class="title"><spring:message code="dashboard.cat.title" /></div><br />
		<div id="categories-container">
			<ul id="categories" class="pointer">
				<c:forEach items="${categoriesList}" var="cur">
					<li id="cat-${cur.id}" class="category">
						<span id="catname-${cur.id}" class="category-name"><c:out value="${cur.name}" /></span>
						<span id="catcount-${cur.id}" class="category-count"></span>
						<span id="catmenu-${cur.id}" class="category-menus">
							<img id="catrnm-${cur.id}" src="<spring:url value="/images/logged/cat-edit.gif" />" />
							<img id="catrmv-${cur.id}" src="<spring:url value="/images/logged/cat-remove.png" />" />
						</span>
					</li>
				</c:forEach>
			</ul>
			<div id="cat-unclassified" class="category category-selected">
				<span id="catname-0"><spring:message code="dashboard.cat.unclassified" /></span>
				<span id="catcount-0" class="category-count"></span>
			</div>
			<div id="no-category"><spring:message code="dashboard.cat.nothing" /></div>
		</div>
		<%-- Categories edition link --%>
		<div id="categories-edit-link">
			<a id="categories-edit">
				<spring:message code="dashboard.cat.edit" /> <img src="<spring:url value="/images/logged/edit.gif" />" />
			</a>
		</div>
		<%-- Categories edition container --%>
		<div id="categories-edit-container" class="hide">
			<b><spring:message code="dashboard.cat.insert" /></b><br />
			<input id="catadd-name" type="text" class="category" maxlength="64" /><br />
			<i><spring:message code="dashboard.cat.insert.help" /></i><br />
			<a id="categories-endedit">
				<spring:message code="dashboard.cat.finEdit" /> <img src="<spring:url value="/images/logged/grey-tick.png" />" />
			</a>
		</div>
		<br />
	</div>

	<%-- Right --%>
	<div id="notes-container" class="span-17 last">
		<div id="insert-note-container" class="top-frames">
			<input id="quick-add-task" class="clearField clearFieldBlurred" type="text" value="<spring:message code="dashboard.note.quickInsert" />" maxlength="255" />
		</div><br /><br />

		<%-- Missed notes --%>
		<c:if test="${fn:length(missedList) > 0}">
			<div class="title"><spring:message code="dashboard.note.missedTitle" /></div><br />
			<c:forEach items="${missedList}" var="cur">
				<c:import url="/WEB-INF/views/logged/dashboard-notes.jsp">
					<c:param name="id">${cur.id}</c:param>
					<c:param name="name">${cur.name}</c:param>
				</c:import>
			</c:forEach>
			<br />
		</c:if>

		<%-- Undone notes --%>
		<div <c:if test="${(typeDashboard != 'TODAY' && fn:length(notesList) == 0 && fn:length(doneList) != 0) || (typeDashboard == 'TODAY' && fn:length(notesList) == 0 && (fn:length(doneList) != 0 || (fn:length(doneList) == 0 && fn:length(missedList) != 0)))}">class="hide"</c:if>>
			<%-- Title --%>
			<div class="title">
				<c:choose>
					<c:when test="${typeDashboard == 'TODAY'}">
						<spring:message code="dashboard.note.todayTitle" />
					</c:when>
					<c:when test="${typeDashboard == 'TOMORROW'}">
						<spring:message code="dashboard.note.tomorrowTitle" />
					</c:when>
					<c:when test="${typeDashboard == 'THIS_WEEK'}">
						<spring:message code="dashboard.note.weekTitle" />
					</c:when>
					<c:otherwise>
						<spring:message code="dashboard.note.title" />
					</c:otherwise>
				</c:choose>
			</div><br />
			<%-- No note to do --%>
			<div>
				<c:if test="${fn:length(notesList) == 0}">
					<c:if test="${typeDashboard == 'GLOBAL'}">
						<div class="no-data"><spring:message code="dashboard.note.nothing" /></div>
					</c:if>
					<c:if test="${typeDashboard != 'GLOBAL'}">
						<div class="no-data">
							<c:choose>
								<c:when test="${typeDashboard == 'TODAY'}">
									<spring:message code="dashboard.note.nothingToday" />
								</c:when>
								<c:when test="${typeDashboard == 'TOMORROW'}">
									<spring:message code="dashboard.note.nothingTomorrow" />
								</c:when>
								<c:when test="${typeDashboard == 'THIS_WEEK'}">
									<spring:message code="dashboard.note.nothingWeek" />
								</c:when>
							</c:choose>
							<br /><a href="<spring:url value="/dashboard" />"><spring:message code="dashboard.note.backDashboard" /></a>.
						</div>
					</c:if>
				</c:if>
				<%-- Undone notes list --%>
				<c:forEach items="${notesList}" var="cur">
					<c:import url="/WEB-INF/views/logged/dashboard-notes.jsp">
						<c:param name="id">${cur.id}</c:param>
						<c:param name="name">${cur.name}</c:param>
					</c:import>
				</c:forEach>
			</div>
			<br />
		</div>

		<%-- Done notes --%>
		<div <c:if test="${fn:length(doneList) == 0}">class="hide"</c:if>>
			<%-- Title --%>
			<div class="title">
				<c:choose>
					<c:when test="${typeDashboard == 'TODAY'}">
						<spring:message code="dashboard.note.doneTodayTitle" />
					</c:when>
					<c:when test="${typeDashboard == 'TOMORROW'}">
						<spring:message code="dashboard.note.doneTomorrowTitle" />
					</c:when>
					<c:when test="${typeDashboard == 'THIS_WEEK'}">
						<spring:message code="dashboard.note.doneWeekTitle" />
					</c:when>
					<c:otherwise>
						<spring:message code="dashboard.note.recentlyDoneTitle" />
					</c:otherwise>
				</c:choose>
			</div><br />

			<%-- Done notes list --%>
			<c:forEach items="${doneList}" var="cur">
				<c:import url="/WEB-INF/views/logged/dashboard-notes.jsp">
					<c:param name="id">${cur.id}</c:param>
					<c:param name="name">${cur.name}</c:param>
					<c:param name="checked">true</c:param>
				</c:import>
			</c:forEach>
			<br />
		</div>
	</div>

	<%-- Preload some images --%>
	<img class="hide" src="<spring:url value="/images/logged/cat-bg-to-note.png" />" />
</body>
</html>

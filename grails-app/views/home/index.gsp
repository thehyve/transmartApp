<!DOCTYPE HTML>
<html>
<head>
    <title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
    <meta name="layout" content="main">
    <g:if env="development">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
    </g:if>
</head>

<body>
<p>You are logged in as <strong><g:encodeAs codec="html">${user}</g:encodeAs></strong>.</p>

<ul>
    <g:if test="${isAdmin}">
        <li><g:link controller="accessLog">Admin Panel</g:link></li>
    </g:if>
    <li><g:link controller="changeMyPassword">Change Password</g:link></li>
    <li><g:link controller="logout">Logout</g:link></li>
</ul>
</body>
</html>

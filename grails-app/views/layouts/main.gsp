<html>
<head>
    <title><g:layoutTitle default="tranSMART"/></title>
    <r:external uri="images/searchtool.ico"/>
    <r:require module="main_mod" />
    <r:script type="text/javascript" charset="utf-8" disposition="head">
        Ext.BLANK_IMAGE_URL = '${resource(dir:'js', file:'ext/resources/images/default/s.gif').encodeAsJavaScript()}';

        // set ajax to 90*1000 milliseconds
        Ext.Ajax.timeout = 180000;

        // qtip on
        Ext.QuickTips.init();
    </r:script>
    <g:layoutHead/>
    <r:layoutResources/>
</head>

<body>
<g:layoutBody/>
<r:layoutResources/>
</body>
</html>

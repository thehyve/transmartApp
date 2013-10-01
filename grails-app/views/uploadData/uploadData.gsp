<%--
  tranSMART - translational medicine data mart

  Copyright 2008-2012 Janssen Research & Development, LLC.

  This product includes software developed at Janssen Research & Development, LLC.

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
  as published by the Free Software  * Foundation, either version 3 of the License, or (at your option) any later version, along with the following terms:
  1.  You may convey a work based on this program in accordance with section 5, provided that you retain the above notices.
  2.  You may convey verbatim copies of this program code as you receive it, in any medium, provided that you retain the above notices.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS    * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.


--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Strict//EN">
<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'searchtool.ico')}">
    <link rel="icon" href="${resource(dir:'images',file:'searchtool.ico')}">
    <link rel="stylesheet" href="${resource(dir:'js', file:'ext/resources/css/ext-all.css')}">
    <link rel="stylesheet" href="${resource(dir:'js', file:'ext/resources/css/xtheme-gray.css')}">
    <link rel="stylesheet" href="${resource(dir:'css', file:'main.css')}">
    <link rel="stylesheet" href="${resource(dir:'css', file:'uploadData.css')}">

    <!--[if IE 7]>
      <style type="text/css">
         div#gfilterresult,div#ptfilterresult, div#jubfilterresult, div#dqfilterresult {
          width: 99%;
        }
      </style>
    <![endif]-->

    <g:javascript library="prototype" />
    <script type="text/javascript" src="${resource(dir:'js', file:'ext/adapter/ext/ext-base.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js', file:'ext/ext-all.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js', file:'ext/miframe.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js', file:'searchcombobox.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js', file:'picklist.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js', file:'utilitiesMenu.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js', file:'jQuery/jquery.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js', file:'jQuery/jquery-ui.min.js')}"></script>
    <script type="text/javascript">$j = jQuery.noConflict();</script>
    <script type="text/javascript" src="${resource(dir:'js', file:'uploadData.js')}"></script>
    <script type="text/javascript" charset="utf-8">
      var studyBrowseWindowUrl = '${createLink([controller: 'experiment', action: 'browseExperimentsSingleSelect'])}';
      var studyDetailUrl = '${createLink([controller:'experimentAnalysis', action:'expDetail'])}';
      var platformTypesUrl = '${createLink([action:'platformsForVendor',controller:'bioAssayPlatform'])}';
      var templateDownloadUrl = '${createLink([action:'template',controller:'uploadData'])}';

      var IS_EDIT = ${uploadDataInstance?.id ? true : false};
      var ANALYSIS_TYPE = null;

      Ext.BLANK_IMAGE_URL = "${resource(dir:'js', file:'ext/resources/images/default/s.gif')}";

      // set ajax to 90*1000 milliseconds
      Ext.Ajax.timeout = 180000;

      // qtip on
      Ext.QuickTips.init();

      Ext.onReady(function(){
          var helpURL = '${grailsApplication.config.com.recomdata.searchtool.adminHelpURL}';
          var contact = '${grailsApplication.config.com.recomdata.searchtool.contactUs}';
          var appTitle = '${grailsApplication.config.com.recomdata.searchtool.appTitle}';
          var buildVer = 'Build Version: <g:meta name="environment.BUILD_NUMBER"/> - <g:meta name="environment.BUILD_ID"/>';

        var viewport = new Ext.Viewport({
          layout: "border",
          items:[new Ext.Panel({
               region: "center",
               tbar: createUtilitiesMenu(helpURL, contact, appTitle,'${request.getContextPath()}', buildVer, 'utilities-div'),
               contentEl: "uploadArea"
              })
              ]
        });
        viewport.doLayout();
      });

      <g:if test="${study}">
        updateStudyTable('${study.accession}');
      </g:if>
    </script>
    <title>${grailsApplication.config.com.recomdata.dataUpload.appTitle}</title>
    <!-- ************************************** -->
        <!-- This implements the Help functionality -->
        <script type="text/javascript" src="${resource(dir:'js', file:'help/D2H_ctxt.js')}"></script>
        <script type="text/javascript">
          helpURL = '${grailsApplication.config.com.recomdata.searchtool.adminHelpURL}';
        </script>
    <!-- ************************************** -->
  </head>
  <body>
    <div id="header-div">
      <g:render template="/layouts/commonheader" model="['app':'uploaddata']" />
    </div>

    <div id="uploadArea">
      <g:uploadForm name="dataUpload" action="upload" method="post">
        <g:render template="formPage1" />
        <g:render template="formPage2" />
        <g:hiddenField name="id" value="${uploadDataInstance?.id}"/>
      </g:uploadForm>
    </div>

    <!-- Browse dialog -->
    <div id="divBrowseStudies" title="Studies">
      <img src="${resource(file:'ajax-loader.gif', dir:'images')}"/>
    </div>

  </body>
</html>

<div class="body" style="clear:both; width:99%;margin-left:5px;margin-top:20px">
<table style="border:0px; align:left">
    <tr>
        <td style="width: 125px; padding-right:0px; vertical-align:middle">
            <img src="${resource(dir:'images',file:grailsApplication.config.com.recomdata.searchtool.smallLogo)}" alt="tranSMART" style="position: relative; top: 8px;" />
        </td>
        <td style="width: 10px; vertical-align:middle;padding-left:0px; padding-right:0px;">
            <img src="${resource(dir:'images',file:'c-med.gif')}" alt="arrow" style="position: relative; top: 8px;" />
        </td>
        <td valign="middle" style="vertical-align:middle; border:1px; padding-right:0px; font-size:11px" nowrap="nowrap">
            <div style="width:650px;">
                <div class="x-box-tl">
                    <div class="x-box-tr">
                        <div class="x-box-tc">
                        </div>
                    </div>
                </div>
                <div class="x-box-ml">
                    <div class="x-box-mr">
                        <div class="x-box-mc">
                                <table style="border:0px; align:center;">
                                <tr>
                                    <td colspan="3" style="padding-bottom: 0px; padding-top: 0px;">
                                        <div id="search-categories"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div id="search-text"></div>
                                        <div id="loading-div" style="visibility:hidden;display:none;">
                                            <input type="text" id="loading-text"
                                                style="width:452px;color:background-image:url(js/ext/resources/images/default/grid/loading.gif);background-repeat:no-repeat;background-position:left center;padding-left:20px;"
                                            />
                                        </div>
                                    </td>
                                    <td style="white-space:nowrap;" nowrap>
                                        <g:form name="form" controller="search" action="search">
                                            <g:hiddenField id="id-field" name="id" value="" />
                                            <g:hiddenField name="sourcepage" value="index" />
                                            <button id="search-button" type="button" onclick="searchOnClick();" style="vertical-align:middle;">Search</button>
                                            &nbsp;
                                        </g:form>
                                    </td>
                                    <td style="vertical-align:middle">
                                        <div id="linkbuttons-div">
                                            <a id="browse-link" class="tiny" style="text-decoration:underline;color:blue;font-size:11px;" href=""
                                                onclick="popupWindow('${createLink(controller:'searchHelp', action:'list')}', 'searchhelpwindow');return false;">
                                                browse
                                            </a>
                                            <br />
                                            <a id="savedfilters-link" class="tiny" style="text-decoration:underline;color:blue;font-size:11px;"
                                                href="${createLink(controller:'customFilter', action:'list')}">
                                                <nobr>saved filters</nobr>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="x-box-bl">
                    <div class="x-box-br">
                        <div class="x-box-bc">
                        </div>
                    </div>
                </div>
            </div>

        </td>
        <td>
            <%topicID="1013" %>
            <a HREF='JavaScript:D2H_ShowHelp(<%=topicID%>,helpURL,"wndExternal",CTXT_DISPLAY_FULLHELP )'>
                <img src="${resource(dir:'images',file:'help/helpbutton.jpg')}" alt="Help" border=0 width=18pt style="margin-top:10pt;margin-bottom:10pt;margin-right:18pt;float:right"/>
            </a>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
        </td>
    </tr>
</table>
</div>
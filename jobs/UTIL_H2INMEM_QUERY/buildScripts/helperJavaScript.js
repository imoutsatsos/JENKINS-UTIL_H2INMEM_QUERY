/*
TEST_PROCESS_IMAGELIST: HelperJavaScript.js

Introduces DOM manipulation with jQuery to enhance the creation of the ANNOTATION_PLAN
ANNOTATION_PLAN no longer shares the same dropdown as PROPERTIES but gets a dedicated cloned copy
@Since JAN-09-2019
@Author: Ioannis K. Moutsatsos
*/

/*  wait for the document to be fully loaded before working with it */

jQuery(document).ready(function() {


});

/* Function creates map when both key and value are provided from  CV selection */

function addPlannedAction(node, plan, action, prop, oper, value) {
  var planAction = document.getElementById(action).value;
  var dsProp = document.getElementById(prop).value;
  console.log('#' + prop)
  var dsOper = document.getElementById(oper).value;
  if (value == 'annoSelector') {
    var propValue = Q(jQuery('#' + value)).find('select').val();
  } else {
    var propValue = document.getElementById(value).value;
  }
  var planVal = '';
  planVal = planVal.concat('[', "action:", '"', planAction, '",', "operator:", '"', dsOper, '",', "property:", '"', dsProp, '",', "value:", '"', propValue, '"', ']');
  console.log('queryPlanValue:' + planVal)
  document.getElementById(plan).value = document.getElementById(plan).value.concat(planVal);
  parentId = document.getElementById(plan).parentNode.id
  jQuery('#' + parentId).trigger('change')
  /* add checkbox option to document node */
  addAnnotationCheckBox(node, planAction, dsProp, dsOper, propValue, planVal)
}

/* a generic function to add an annotation action checkbox ---------------------------------------*/

function addAnnotationCheckBox(planNode, mNode, mKey, mOper, mValue, planValue) {
  var dNode = document.getElementById(planNode);
  var x = document.createElement("INPUT");
  x.setAttribute("type", "checkbox");
  x.setAttribute("name", planNode + "Action");
  x.setAttribute("value", planValue);
  dNode.appendChild(x);
  var y = document.createElement("label");
  y.setAttribute("class", "attach-previous");
  var labelFor = mNode + '.' + mKey + '.' + mValue;
  x.title = labelFor;
  y.setAttribute("for", labelFor);
  y.innerHTML = mNode + '.' + mKey + mOper.replace('equal', '=') + mValue + '<br/>';
  dNode.appendChild(y);
}

/* the action is deleted from checklist as well as from hidden xtPlan text input */
function deletePlannedAction(plan, planNode) {
  jQuery(document).ready(function() {
    var selected = [];
    console.log("input[name='" + planNode + "Action']:checked");
    jQuery.each(jQuery("input[name='" + planNode + "Action']:checked"), function() {
      selected.push(jQuery(this).val());
      delEntry = jQuery(this).val();
      console.log("Deleting:" + delEntry);
      document.getElementById(plan).value = document.getElementById(plan).value.replace(delEntry, '');
      parentId = document.getElementById(plan).parentNode.id;
      jQuery('#' + parentId).trigger('change');
      label2remove = jQuery(this).attr('title');
      console.log("Removing:" + label2remove);
      jQuery('label[for="' + label2remove + '"]').remove();
      jQuery(this).remove();
    });
    //alert("Deleted: " +selected.join(", "));
  });
}

function resetPlannedAction(plan, planNode) {
  document.getElementById(plan).value = '';
  jQuery(document).ready(function() {
    console.log("input[name='" + planNode + "Action']:checked");
    jQuery.each(jQuery("input[name='" + planNode + "Action']"), function() {
      label2remove = jQuery(this).attr('title');
      jQuery('label[for="' + label2remove + '"]').remove();
      jQuery(this).remove();
    });
    parentId = document.getElementById(plan).parentNode.id;
    jQuery('#' + parentId).trigger('change');
  });
}

/* note that a similar function exists in SCRIPT_HELPER
function setQueryFlag() {
  console.log('calling2On')
  document.getElementById("queryFlag").value = 'ON'
  parentId = document.getElementById("queryFlag").parentNode.id
  jQuery('#' + parentId).trigger('change')
}
*/

function setProcessFlag() {
  console.log('setting_processFlag_2On')
  document.getElementById("processFlag").value = 'ON'
  parentId = document.getElementById("processFlag").parentNode.id
  jQuery('#' + parentId).trigger('change')
}

/* 
assembles a json string of other parameters
and from it sets the value of the transfer parameter

jsonParams is used by the: 
	imageMagickProcessor_jsonInput.groovy
*/
function setJsonTransferParam(transferParamId) {
  //TEST_IMAGES,OVERLAY,GLOBAL_IMPATH,GLOBAL_SESSIONS_WORKSPACE
  console.log('setting_json_parameters')
  jsonParams = {
    TEST_IMAGES: joinObj(jQuery('#' + jQuery('input[value=TEST_IMAGES]')[0].nextSibling.id).find('option:selected'), 'value'),
    OVERLAY: jQuery('#imgPerComposite').val(),
    GLOBAL_IMPATH: document.getElementById('imagickPath').value,
    GLOBAL_SESSIONS_WORKSPACE: document.getElementById('sessionsWorkspace').value,
    cantaloupeLocalBaseUrl: document.getElementById('cantaloupeLocalBaseUrl').value,
    cantaloupeLocalConfig: document.getElementById('cantaloupeLocalConfig').value,
    vOutputFolder: document.getElementById('jobSessionPath').value,
  }
  document.getElementById(transferParamId).value = JSON.stringify(jsonParams)
  transferParentId = document.getElementById(transferParamId).parentNode.id
  jQuery('#' + transferParentId).trigger('change')
}

/* 
assembles a json string of other parameters
and from it sets the value of the transfer parameter
images2View: options are i2v (TEST_IMAGES) i2conv (PROCESSED_IMAGES)

jsonParams is used by the: 
	JSON_GALLERIES_imageGalleryFromTemplate.groovy
*/
function setJsonGalleryParam(transferParamId, images2View) {
  //vOutputFolder,vIMAGELIST_URL,vIMAGE_OBJECTS,vIMAGE_GALLERY,vPRIMARY_IMAGE_LIST,vJOB_PATH,vIMAGE_ADJUSTMENTS,vTEST_IMAGES,vBUILD_LABEL
  console.log('setting_json_parameters')
  gallerySource=images2View
  galleryPath=(gallerySource=='i2conv')?jQuery('#galleryPath').val():'imageList'
  galleryImages=(gallerySource=='i2conv')?document.getElementById('i2convArchive').value:document.getElementById(images2View).value
  gallerySuffix=(gallerySource=='i2conv')?'_LevelIntensities':''
  objNames = jQuery('#' + jQuery('input[value=IMAGE_OBJECTS]')[0].nextSibling.id).find('input:checked').map(function() {
    return Q(this).val()
  })
  objOpacity = jQuery('#' + jQuery('input[value=IMAGE_OBJECTS]')[0].nextSibling.id).find('input:checked').map(function() {
    return 'opacity_' + Q(this).val()
  }).map(function() {
    return Q('#' + this).val()
  })
  objColor = jQuery('#' + jQuery('input[value=IMAGE_OBJECTS]')[0].nextSibling.id).find('input:checked').map(function() {
    return 'colpick_' + Q(this).val()
  }).map(function() {
    return Q('#' + this).val()
  })
  
  var d = new Date();
  var timestamp=d.getFullYear()+("0"+(d.getMonth()+1)).slice(-2)+("0"+d.getDate()).slice(-2)+ ("0" + d.getHours()).slice(-2)+ ("0" + d.getMinutes()).slice(-2)+("0" + d.getSeconds()).slice(-2);
  
  jsonParams = {
    vOutputFolder: document.getElementById('jobSessionPath').value,
    vIMAGELIST_URL: jQuery('#' + jQuery('input[value=IMAGELIST_URL]')[0].nextSibling.id).find('input')[0].value,
    vTEST_IMAGES: galleryImages,
    vIMAGE_LABELS: document.getElementById('i2vlabels').value,
    vPRIMARY_IMAGE_LIST: jQuery(jQuery('input[value=PRIMARY_IMAGE_LIST]')[0].nextSibling).find('option:selected').val(),
    vLAYOUT: document.getElementById('gridRows').value.concat(',', jQuery('input[name=fillBy]').filter(':checked').val()),
    vIMAGE_OBJECTS: {
      NAMES: Object.values(objNames).slice(0, objNames.length).join(','),
      COLOR: Object.values(objColor).slice(0, objColor.length).join(','),
      OPACITY: Object.values(objOpacity).slice(0, objOpacity.length).join(','),
    },   
    vBRIGHTNESS: jQuery('#set_brightness').val(),
    vCONTRAST: jQuery('#set_contrast').val(),
    LAYOUT: {
      vFILLSIZE: jQuery('#gridRows').val(),
      vFILLBY: jQuery('[name^=fillBy]:checked').val()
    },
    OVERLAY: {
      OVERLAYFLAG: jQuery('#getOverlay').val(),
      OVERLAYSIZE: jQuery('#imgPerComposite').val()
    },
    vNOTES: jQuery('input[value=GALLERY_NOTES]')[0].nextSibling.value,
    TSTAMP: timestamp,
    GALLERY_SOURCE: gallerySource,
    GALLERY_PATH: galleryPath,
    vBUILD_LABEL: jQuery('#' + jQuery('input[value=GALLERY_LABEL]')[0].nextSibling.id).find('input')[0].value+gallerySuffix
  }
  document.getElementById(transferParamId).value = JSON.stringify(jsonParams)
  transferParentId = document.getElementById(transferParamId).parentNode.id
  jQuery('#' + transferParentId).trigger('change')
}

/*assembles a json object for the current result set */
function setJsonResultParam(transferParamId){
      //resultSetId: resultSetId,
  jsonParams = {
    queryId: document.getElementById("lastResult").value,
    artifactPath: document.getElementById("artifactPath").value,
    artifactLabel:jQuery('#' + jQuery('input[value=RESULT_LABEL]')[0].nextSibling.id).find('input')[0].value,
    resultSetURL: document.getElementById("dataCsv").value,
    resultSetColumns: document.getElementById("dataCols").value,
    TSTAMP: getTimeStamp(),
  }
  document.getElementById(transferParamId).value = JSON.stringify(jsonParams)
  transferParentId = document.getElementById(transferParamId).parentNode.id
  jQuery('#' + transferParentId).trigger('change')
}

/*
returns selected values of an AC parameter
as a list of comma separated values 
similar to how AC cascade  values
*/
function joinObj(a, attr) {
  var out = [];

  for (var i = 0; i < a.length; i++) {
    out.push(a[i][attr]);
  }

  return out.join(", ");
}

/* returns a time stamp
to be used in JSON objects
*/

function getTimeStamp(){
  d = new Date();
 return d.getFullYear()+("0"+(d.getMonth()+1)).slice(-2)+("0"+d.getDate()).slice(-2)+ ("0" + d.getHours()).slice(-2)+ ("0" + d.getMinutes()).slice(-2)+("0" + d.getSeconds()).slice(-2);
}

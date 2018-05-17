(inform-jar "modules/editor.pdf/dist/editor.msoffice.jar" "cc.fooledit.editor.msoffice.MsOfficeModule" "onInstall")

(map-mime-to-type "application/vnd.ms-excel" "cc.fooledit.editor.msoffice.excel.XlsObjectType")
(map-suffix-to-mime "xls" "application/vnd.ms-excel")
(map-suffix-to-mime "xlc" "application/vnd.ms-excel")
(map-suffix-to-mime "xll" "application/vnd.ms-excel")
(map-suffix-to-mime "xlm" "application/vnd.ms-excel")
(map-suffix-to-mime "xlw" "application/vnd.ms-excel")
(map-suffix-to-mime "xla" "application/vnd.ms-excel")
(map-suffix-to-mime "xlt" "application/vnd.ms-excel")
(map-suffix-to-mime "xld" "application/vnd.ms-excel")
(mime-alias "application/msexcel" "application/vnd.ms-excel")
(mime-alias "application/x-msexcel" "application/vnd.ms-excel")
(mime-alias "zz-application/zz-winassoc-xls" "application/vnd.ms-excel")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" "cc.fooledit.editor.msoffice.powerpoint.XlsObjectType")
(map-suffix-to-mime "xlsx" "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.spreadsheetml.template" "cc.fooledit.editor.msoffice.powerpoint.XlsObjectType")
(map-suffix-to-mime "xltx" "application/vnd.openxmlformats-officedocument.spreadsheetml.template")

(map-mime-to-type "application/vnd.ms-word" "cc.fooledit.editor.msoffice.word.DocObjectType")
(map-suffix-to-mime "doc" "application/vnd.ms-word")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.wordprocessingml.document" "cc.fooledit.editor.msoffice.word.DocxObjectType")
(map-suffix-to-mime "docx" "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.wordprocessingml.template" "cc.fooledit.editor.msoffice.word.DocxObjectType")
(map-suffix-to-mime "dotx" "application/vnd.openxmlformats-officedocument.wordprocessingml.template")

(map-mime-to-type "application/vnd.ms-powerpoint" "cc.fooledit.editor.msoffice.powerpoint.PptObjectType")
(map-suffix-to-mime "ppt" "application/vnd.ms-powerpoint")
(map-suffix-to-mime "pps" "application/vnd.ms-powerpoint")
(map-suffix-to-mime "pot" "application/vnd.ms-powerpoint")
(map-suffix-to-mime "ppz" "application/vnd.ms-powerpoint")
(mime-alias "application/powerpoint" "application/vnd.ms-powerpoint")
(mime-alias "application/mspowerpoint" "application/vnd.ms-powerpoint")
(mime-alias "application/x-mspowerpoint" "application/vnd.ms-powerpoint")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.presentationml.presentation" "cc.fooledit.editor.msoffice.powerpoint.PptxObjectType")
(map-suffix-to-mime "pptx" "application/vnd.openxmlformats-officedocument.presentationml.presentation")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.presentationml.slideshow" "cc.fooledit.editor.msoffice.powerpoint.PptxObjectType")
(map-suffix-to-mime "ppsx" "application/vnd.openxmlformats-officedocument.presentationml.slideshow")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.presentationml.template" "cc.fooledit.editor.msoffice.powerpoint.PptxObjectType")
(map-suffix-to-mime "potx" "application/vnd.openxmlformats-officedocument.presentationml.template")
(map-mime-to-type "application/vnd.openxmlformats-officedocument.presentationml.slide" "cc.fooledit.editor.msoffice.powerpoint.PptxObjectType")
(map-suffix-to-mime "sldx" "application/vnd.openxmlformats-officedocument.presentationml.slide")

(map-mime-to-type "application/vnd.ms-publisher" "cc.fooledit.editor.msoffice.publisher.PubObjectType")
(map-suffix-to-mime "pub" "application/vnd.ms-publisher")

(map-mime-to-type "application/vnd.visio" "cc.fooledit.editor.msoffice.visio.VsdObjectType")
(map-suffix-to-mime "vsd" "application/vnd.visio")
(map-suffix-to-mime "vst" "application/vnd.visio")
(map-suffix-to-mime "vsw" "application/vnd.visio")
(map-suffix-to-mime "vss" "application/vnd.visio")
(map-mime-to-type "application/vnd.ms-visio.drawing.main+xml" "cc.fooledit.editor.msoffice.powerpoint.VsdxObjectType")
(map-suffix-to-mime "vsdx" "application/vnd.ms-visio.drawing.main+xml")
(map-mime-to-type "application/vnd.ms-visio.template.main+xml" "cc.fooledit.editor.msoffice.powerpoint.VsdxObjectType")
(map-suffix-to-mime "vstx" "application/vnd.ms-visio.template.main+xml")
(map-mime-to-type "application/vnd.ms-visio.stencil.main+xml" "cc.fooledit.editor.msoffice.powerpoint.VsdxObjectType")
(map-suffix-to-mime "vssx" "application/vnd.ms-visio.stencil.main+xml")
(map-mime-to-type "application/vnd.ms-visio.drawing.macroEnabled.main+xml" "cc.fooledit.editor.msoffice.powerpoint.VsdxObjectType")
(map-suffix-to-mime "vsdm" "application/vnd.ms-visio.drawing.macroEnabled.main+xml")
(map-mime-to-type "application/vnd.ms-visio.template.macroEnabled.main+xml" "cc.fooledit.editor.msoffice.powerpoint.VsdxObjectType")
(map-suffix-to-mime "vstm" "application/vnd.ms-visio.template.macroEnabled.main+xml")
(map-mime-to-type "application/vnd.ms-visio.stencil.macroEnabled.main+xml" "cc.fooledit.editor.msoffice.powerpoint.VsdxObjectType")
(map-suffix-to-mime "vssm" "application/vnd.ms-visio.stencil.macroEnabled.main+xml")

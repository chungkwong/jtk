(import (java))
(invoke-static 'cc.fooledit.editor.zip.ZipModule 'onLoad)
(map-mime-to-type "application/x-7z-compressed" "archive")
(map-mime-to-type "application/x-archive" "archive")
(map-mime-to-type "application/x-arj" "archive")
(map-mime-to-type "application/x-bzip" "zip")
(map-mime-to-type "application/x-bzip2" "zip")
(map-mime-to-type "application/x-cpio" "archive")
(map-mime-to-type "application/x-gtar" "archive")
(map-mime-to-type "application/gzip" "zip")
(map-mime-to-type "application/x-gzip" "zip")
(map-mime-to-type "application/java-archive" "archive")
(map-mime-to-type "application/x-java-archive" "archive")
(map-mime-to-type "application/x-jar" "archive")
(map-mime-to-type "application/x-java-pack200" "zip")
(map-mime-to-type "application/x-lz4" "zip")
(map-mime-to-type "application/x-lzma" "zip")
(map-mime-to-type "application/x-tar" "archive")
(map-mime-to-type "application/x-xz" "zip")
(map-mime-to-type "application/zip" "archive")
(map-mime-to-type "application/x-zip-compressed" "archive")
(map-mime-to-type "application/zlib" "zip")

;TODO: DUMP,Brotli,DEFLATE,LZ77,LZW,Snappy
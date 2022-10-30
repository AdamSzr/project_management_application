package utp.agile.kerplank.domain.drive

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import utp.agile.kerplank.configuration.DriveConfiguration
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString


@RestController
@RequestMapping("/api/drive")
class DriveController(val driveConfiguration: DriveConfiguration, val driveService: DriveService) {


    @GetMapping("/path")
    fun getMyDirectory(): ResponseEntity<String> {
        return ResponseEntity<String>(Path(driveConfiguration.directory).absolutePathString(), HttpStatus.OK)
    }


    @GetMapping("/directory")
    fun listDirectory(
        @RequestParam path: String
    ): Any {
        driveService.listDirectoryItems(path).let {
            if (it.isNullOrEmpty())
                return ResponseEntity<String>(
                    "Wrong path to directory", null, HttpStatus.BAD_REQUEST
                )
            else
                return ResponseEntity<List<DriveService.DirectoryItem>>(
                    it, null, HttpStatus.OK
                )
        }
    }


    @GetMapping(
        "file", produces = [
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.ALL_VALUE]
    )
    fun getMyFile(
        @RequestParam("path") pathToFile: String
    ): Any {

        val pathObj = Path(driveConfiguration.directory + pathToFile)
        val mime = Files.probeContentType(pathObj)
        val header = HttpHeaders()
            .apply { set(HttpHeaders.CONTENT_TYPE, mime) }
            .apply { set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${pathToFile.split("/").last()}") }

        return driveService.readFile(pathToFile)
            .let {
                when (it.result.isSuccess) {
                    true -> ResponseEntity<ByteArray>(it.result.getOrThrow(), header, HttpStatus.OK)
                    else -> ResponseEntity<String>(
                        it.result.exceptionOrNull()?.message ?: "Error occurred",
                        header,
                        HttpStatus.OK
                    )
                }
            }
    }



    @PostMapping("upload")
    fun saveFile(@RequestPart("book") book:String, @RequestPart("file") file:Mono<FilePart> ): Mono<Void> {
       return file.flatMap { it.transferTo(File(driveConfiguration.directory+"/"+it.filename())) }
    }

}

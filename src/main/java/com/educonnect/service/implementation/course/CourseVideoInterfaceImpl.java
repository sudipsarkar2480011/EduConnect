package com.educonnect.service.implementation.course;

import com.educonnect.exception.custom_exceptions.FileException;
import com.educonnect.exception.custom_exceptions.UserIdDoNothMatchException;
import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.model.course.ModuleType;
import com.educonnect.model.user.Student;
import com.educonnect.repo.EnrollmentRepo;
import com.educonnect.repo.course.CourseModuleRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.course.CourseVideoInerface;
import com.educonnect.utils.video.VideoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseVideoInterfaceImpl implements CourseVideoInerface {


    @Value("${file.upload-dir-one}")
    private String uploadDir;

    @Value("${file.tempUpload-dir-one}")
    private String tempUploadDir;

    private final CourseModuleRepo courseModuleRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;
    @Override
    public CourseModule uploadVideo(MultipartFile file, String title, UUID courseId, UUID teacherId) throws IOException, EncoderException, UserIdDoNothMatchException {
        File tempFile = null;
        Path tempPath=null;
        Path finalFilePath=null;

        try{
           if(file==null || file.isEmpty())
           {
               throw new FileNotFoundException("file not found");
           }
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new IOException("Course does not exist"));
            if (!course.getTeacher().getUserId().equals(teacherId)) {
                throw new UserIdDoNothMatchException("Unauthorized: Only the course creator can upload modules.");
            }
            String extension = file.getOriginalFilename()
                    .substring(file.getOriginalFilename()
                            .lastIndexOf("."));
            MediaType mediaType=MediaType.parseMediaType(file.getContentType());
            String folder;
            ModuleType m;
            if(mediaType.getType().equals("audio"))
            {
                folder="audio/";
                m=ModuleType.AUDIO;

            }
            else if(mediaType.getType().equals("video"))
            {
                folder="video/";
                m=ModuleType.VIDEO;
            } else if (mediaType.equalsTypeAndSubtype(MediaType.APPLICATION_PDF)) {
                folder="pdf/";
                m=ModuleType.PDF;
            }
            else {
                throw new FileException("Unsupported file type : ");
            }

            Path targetFolderPath = Paths.get(this.uploadDir).resolve(folder);
          Path targetTempFolderPath=Paths.get(this.tempUploadDir).resolve(folder);
            if (!Files.exists(targetFolderPath))
            {
                Files.createDirectories(targetFolderPath);
                System.out.println("created target folder ");
            }
            if(!Files.exists(targetTempFolderPath))
            {
                Files.createDirectories(targetTempFolderPath);
                System.out.println("created temp folder: ");
            }
            UUID uuid = UUID.randomUUID();

            String filename = uuid.toString()  + extension;
             finalFilePath=targetFolderPath.resolve(filename);
            Files.copy(file.getInputStream(),finalFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("files have been copied to original file :");
            double duration = 0.0;
            tempPath = targetTempFolderPath.resolve("temp-" + uuid + extension);
            Files.copy(finalFilePath, tempPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("files have been copied to temp file :");
            if (m != ModuleType.PDF) {
                duration = VideoUtil.getVideoDuration(tempPath.toFile());
                System.out.println("duration calculated");
            }

            var resp = courseModuleRepo.save(CourseModule.builder()
                    .moduleId(uuid)
                    .contentUrl(filename)
                    .course(course)
                    .title(title)
                    .moduleType(m)
                    .duration(duration)
                    .build());


            log.info("course duration 01={}",course.getDuration());

            course.setDuration(
                    (course.getDuration() == null ? 0 : course.getDuration() )
                            + duration);

            courseRepo.save(course);


            log.info("course duration 02={}",course.getDuration());

            return resp;

        }catch (Exception e){
            log.error(e.getMessage());
            if(finalFilePath!=null)
            {
                Files.deleteIfExists(finalFilePath);
                log.info("was issue in saving the file in db so deleted from file system too : ");
            }
            throw e;
        }finally {
            System.out.println("finally block triggered");
            if (tempPath != null) {
                Files.deleteIfExists(tempPath);
                log.info("Temp file deleted successfully: {}", tempPath);
            }
        }

        }
    }



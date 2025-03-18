package com.healthy.backend.service;

import com.healthy.backend.dto.comment.CommentRequest;
import com.healthy.backend.dto.comment.CommentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.CommentMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;


    @Transactional
    public CommentResponse addAppointmentComment(String appointmentId, CommentRequest request, String userId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Kiểm tra xem user đã đánh giá cho appointment này chưa (nếu có rating)
        if (request.getRating() != null) {
            boolean hasExistingRating = commentRepository.existsByAppointmentAndAuthorAndRatingIsNotNull(appointment, user);
            if (hasExistingRating) {
                throw new OperationFailedException("You've already submitted a rating for this appointment");
            }
        }

        // Tạo comment mới
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setAppointment(appointment);
        comment.setAuthor(user);

        if (request.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            comment.setParentComment(parent);
        }

        Comment savedComment = commentRepository.save(comment);

        // Cập nhật thông tin feedback và rating cho appointment
        if (request.getRating() != null) {
            appointment.setRating(request.getRating());
        }
        // Giả sử nếu user là STUDENT, feedback là studentNote; nếu PSYCHOLOGIST thì là psychologistNote
        if (user.getRole() == Role.STUDENT) {
            appointment.setStudentNote(request.getContent());
        } else if (user.getRole() == Role.PSYCHOLOGIST) {
            appointment.setPsychologistNote(request.getContent());
        }
        appointmentRepository.save(appointment);

        return mapToResponse(savedComment);
    }


    private CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getRating(),
                comment.getAuthor().getFullName(),
                comment.getCreatedAt(),
                comment.getReplies().stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList())
        );
    }



    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsForAppointment(String appointmentId) {
        List<Comment> comments = commentRepository.findByAppointment_AppointmentIDAndParentCommentIsNullOrderByCreatedAtAsc(appointmentId);
        return comments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


}

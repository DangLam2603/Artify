package com.example.artworksharingplatform.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.PreOrder;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.PreOrderMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.PreOrderDTO;
import com.example.artworksharingplatform.model.PreOrderRequest;
import com.example.artworksharingplatform.model.ProcessingRequest;
import com.example.artworksharingplatform.service.ArtworkService;
import com.example.artworksharingplatform.service.CloudinaryService;
import com.example.artworksharingplatform.service.EWalletService;
import com.example.artworksharingplatform.service.PostService;
import com.example.artworksharingplatform.service.PreOrderService;
import com.example.artworksharingplatform.service.UserService;;

@RestController
@RequestMapping("api/auth")
public class PreOrderController {
    @Autowired
    UserService _userService;
    @Autowired
    EWalletService _eWalletService;
    @Autowired
    PreOrderService _preOrderService;

    @Autowired
    PreOrderMapper _preOrderMapper;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ArtworkService artworkService;

    @Autowired
    PostService postService;

    @PostMapping("audience/PreOrderRequest")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<?> addPreOder(@RequestBody PreOrderRequest preOrderRequest) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User user = _userService.findByEmail(email);
            // validation CreatorId
            var creator = _userService.getUserById(preOrderRequest.getCreatorId());
            if (creator == null) {
                apiResponse.error("creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
            PreOrder preOrderParse = new PreOrder();
            preOrderParse.setPreOrderAudience(user);
            preOrderParse.setPreOrderCreator(creator);
            preOrderParse.setRequirement(preOrderRequest.getRequirement());
            preOrderParse.setStatus("PENDING");
            preOrderParse.setPreOrderDate(Timestamp.valueOf(LocalDateTime.now()));
            _preOrderService.addPreOrder(preOrderParse);
            PreOrderDTO preOrderDTO = _preOrderMapper.toPreOrderDTO(preOrderParse);
            apiResponse.ok(preOrderDTO);
            return ResponseEntity.ok(preOrderDTO);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("creator/viewAllPreOrder")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getCreatorPreOrderList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getCreatorPreOrderList(creator);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("viewProcessingPreOrder")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getAudiencePreOrderList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getWaitingPreOrderList(creator);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("creator/totalPreOrder")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<Integer>> countCreatorPreOrder() {
        ApiResponse<Integer> apiResponse = new ApiResponse<Integer>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            int numOfPreOrder = _preOrderService.countByPreOrderCreator(creator);
            apiResponse.ok(numOfPreOrder);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("creator/updatePreOrder")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> updatePreOrder(
            @RequestPart(value = "updatedPreOrder") PreOrderDTO updatedPreOrderDTO,
            @RequestPart(value = "preOrderArtWork", required = false) MultipartFile file) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();
        try {
            PreOrder changedPreOrder = new PreOrder();
            if (file != null) {
                Artworks artwork = new Artworks();
                artwork.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                artwork.setImagePath(uploadImage(file));
                artwork.setType("PREORDER");
                postService.addArtwork(artwork);
                changedPreOrder = _preOrderService.updatePreOrderCreator(updatedPreOrderDTO, artwork);
            } else {
                changedPreOrder = _preOrderService.updatePreOrderCreator(updatedPreOrderDTO, null);
            }
            if (changedPreOrder == null) {
                apiResponse.error("Cannot find PreOrder");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            PreOrderDTO changedPreOrderDTO = _preOrderMapper.toPreOrderDTO(changedPreOrder);
            apiResponse.ok(changedPreOrderDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("audience/processing")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> ProcessingPreOrder(@RequestBody ProcessingRequest request) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = _userService.findByEmail(email);
        try {
            if (_eWalletService.isEnoughMoney(user.getId(), request.getPrice())) {
                var result = _preOrderService.processingPreOrderAudience(request);
                if (result == null) {
                    throw new Exception("processing failed");
                }
                PreOrderDTO preOrderDTO = _preOrderMapper.toPreOrderDTO(result);
                apiResponse.ok(preOrderDTO);
            } else {
                throw new Exception("Not enough money in EWallet.");
            }

            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("audience/complete")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> completePreOrder(@RequestBody PreOrderDTO preOrderDTO) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();
        try {
            PreOrder result = _preOrderService.completePreOrderAudience(preOrderDTO);
            if (result == null) {
                throw new Exception("Complete failed");
            }
            PreOrderDTO preOrder = _preOrderMapper.toPreOrderDTO(result);
            apiResponse.ok(preOrder);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("audience/cancel")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<String>> CancelPreOrder(@RequestParam("preorderId") UUID preOrderId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = _userService.findByEmail(email);
        String mess = "";
        try {
            if (authentication.isAuthenticated()) {
                var result = _preOrderService.canclePreOrderAudience(preOrderId);
                mess = "Delete PreOrder Successfully";
                apiResponse.ok(mess);
            } else {
                apiResponse.error("Not authenticated");
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("rawtypes")
    public String uploadImage(MultipartFile file) {
        Map data = cloudinaryService.upload(file);
        String url = data.get("url").toString();
        return url;
    }

    @GetMapping("viewAcceptedPreOrders")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getAcceptedPreOrderList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            String status = "ACCEPTED";
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getAcceptedPreOrderList(creator, status);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("viewPendingPreOrders")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getPendingPreOrderList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            String status = "PENDING";
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getAcceptedPreOrderList(creator, status);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("viewConfirmingPreOrders")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getConfirmingPreOrderList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            String status = "Confirming";
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getAcceptedPreOrderList(creator, status);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("viewCompletedPreOrders")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getCompletedPreOrderList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            String status = "Completed";
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getAcceptedPreOrderList(creator, status);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("creator/viewProcessedPreOrders")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getProcessingPreOrderList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            String status = "PROCESSING";
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getProcessingPreOrderList(creator, status);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("creator/viewPendingCreatorPreOrders")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<List<PreOrderDTO>>> getPendingCreatorPreOrdersList() {
        ApiResponse<List<PreOrderDTO>> apiResponse = new ApiResponse<List<PreOrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User creator = _userService.findByEmail(email);
            String status = "PENDING";
            if (creator == null) {
                apiResponse.error("Creator can not be null");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
            List<PreOrder> preOrderList = _preOrderService.getProcessingPreOrderList(creator, status);
            List<PreOrderDTO> preOrderDTOList = _preOrderMapper.toList(preOrderList);
            apiResponse.ok(preOrderDTOList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/preorder/feedback/create")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> createFeedback(@RequestBody PreOrderDTO preOrderDTO) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();

        try {
            PreOrder preOrder = _preOrderService.feedbackPreOrder(preOrderDTO.getPreOrderId(),
                    preOrderDTO.getAudienceFeedback());
            PreOrderDTO pOrderDTO = _preOrderMapper.toPreOrderDTO(preOrder);

            apiResponse.ok(pOrderDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/preorder/feedback/update")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> updateFeedback(@RequestBody PreOrderDTO preOrderDTO) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();

        try {
            PreOrder preOrder = _preOrderService.feedbackPreOrder(preOrderDTO.getPreOrderId(),
                    preOrderDTO.getAudienceFeedback());
            PreOrderDTO pOrderDTO = _preOrderMapper.toPreOrderDTO(preOrder);

            apiResponse.ok(pOrderDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/preorder/feedback/delete")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> deleteFeedback(@RequestParam UUID preOrderId) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();

        try {
            PreOrder preOrder = _preOrderService.deleteFeedback(preOrderId);
            PreOrderDTO pOrderDTO = _preOrderMapper.toPreOrderDTO(preOrder);

            apiResponse.ok(pOrderDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/preorder/rating/create")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> createRating(@RequestBody PreOrderDTO preOrderDTO) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();

        try {
            PreOrder preOrder = _preOrderService.ratePreOrder(preOrderDTO.getPreOrderId(),
                    preOrderDTO.getAudienceRating());
            PreOrderDTO pOrderDTO = _preOrderMapper.toPreOrderDTO(preOrder);

            apiResponse.ok(pOrderDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/preorder/rating/update")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> updateRating(@RequestBody PreOrderDTO preOrderDTO) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();

        try {
            PreOrder preOrder = _preOrderService.ratePreOrder(preOrderDTO.getPreOrderId(),
                    preOrderDTO.getAudienceRating());
            PreOrderDTO pOrderDTO = _preOrderMapper.toPreOrderDTO(preOrder);

            apiResponse.ok(pOrderDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/preorder/rating/delete")
    @PreAuthorize("hasRole('ROLE_CREATOR') or hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<PreOrderDTO>> deleteRating(@RequestParam UUID preOrderId) {
        ApiResponse<PreOrderDTO> apiResponse = new ApiResponse<PreOrderDTO>();

        try {
            PreOrder preOrder = _preOrderService.deleteRating(preOrderId);
            PreOrderDTO pOrderDTO = _preOrderMapper.toPreOrderDTO(preOrder);

            apiResponse.ok(pOrderDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

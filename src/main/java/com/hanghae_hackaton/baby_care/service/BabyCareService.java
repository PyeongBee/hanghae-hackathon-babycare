package com.hanghae_hackaton.baby_care.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.hanghae_hackaton.baby_care.model.BabySitter;
import com.hanghae_hackaton.baby_care.model.Review;
import com.hanghae_hackaton.baby_care.model.dto.MatchBabySitterResponseDTO;
import com.hanghae_hackaton.baby_care.model.dto.MatchBabySittersRequestDTO;
import com.hanghae_hackaton.baby_care.repository.BabySitterRepository;
import com.hanghae_hackaton.baby_care.repository.ReviewRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.hanghae_hackaton.baby_care.utils.JsonUtils.getJsonArrayFromString;
import static com.theokanning.openai.utils.TikTokensUtil.ModelEnum.GPT_3_5_TURBO_0301;

@Service
@RequiredArgsConstructor
public class BabyCareService {
    @Value("${openai.api.url}")
    private String apiURL; // 기껏 설정해놓고, 안 쓰인 것 같다.

    private final BabySitterRepository babySitterRepository;
    private final OpenAiService openAiService;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public MatchBabySitterResponseDTO getBabySitter(Integer id) {
        BabySitter bs = babySitterRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("id is invalid."));

        List<Review> rvs = reviewRepository.findAllByBabySitterId(bs.getId());
        double avg = getAvgReviewScore(rvs);

        return MatchBabySitterResponseDTO.builder()
                .id(bs.getId())
                .name(bs.getName())
                .score(avg)
                .pay(bs.getPay())
                .old(bs.getOld())
                .profile(bs.getContent())
                .gender(bs.getGender())
                .build();
    }

    @Transactional(readOnly = true)
    public List<MatchBabySitterResponseDTO> getBabySitters() {
        List<BabySitter> bss = babySitterRepository.findAll();
        List<MatchBabySitterResponseDTO> responseDTOs = new ArrayList<>();

        for (BabySitter bs : bss) {
            List<Review> rvs = reviewRepository.findAllByBabySitterId(bs.getId());
            double avg = getAvgReviewScore(rvs);

            MatchBabySitterResponseDTO res = MatchBabySitterResponseDTO.builder()
                    .id(bs.getId())
                    .name(bs.getName())
                    .score(avg)
                    .pay(bs.getPay())
                    .old(bs.getOld())
                    .profile(bs.getContent())
                    .gender(bs.getGender())
                    .build();

            responseDTOs.add(res);
        }

        return responseDTOs;
    }

    @Transactional(readOnly = true)
    public List<MatchBabySitterResponseDTO> matchBabySitters(MatchBabySittersRequestDTO requestDTO) {
        List<MatchBabySitterResponseDTO> responseDTOs = new ArrayList<>();
        List<BabySitter> babySitters = babySitterRepository.findAll();

        ChatMessage userMessage =
                new ChatMessage(ChatMessageRole.USER.value(), getPromptMessage(requestDTO, babySitters));
        List<ChatMessage> messages = List.of(userMessage);

        JSONArray array;
        try {
            String gptResponse = callGpt(messages);
            array = getJsonArrayFromString(gptResponse);

            List<MatchBabySitterResponseDTO> arrays = jsonArrayToList(array);

            for (MatchBabySitterResponseDTO dto : arrays) {
                List<Review> rvs = reviewRepository.findAllByBabySitterId(dto.id());
                double avg = getAvgReviewScore(rvs);

                MatchBabySitterResponseDTO res = getBabySittersResponseDTO(dto, avg, rvs);
                responseDTOs.add(res);
            }
        } catch (Exception e) {
            throw new RuntimeException("GPT call에 실패했습니다.");
        }

        return responseDTOs;
    }

    private double getAvgReviewScore (List<Review> rvs) {
        int sum = 0;
        for (Review rv : rvs) {
            sum = sum + rv.getScore();
        }
        double avg = 0.0;
        if (rvs.size() > 0) {
            avg = (double)(sum/rvs.size());
        }

        return avg;
    }

    private MatchBabySitterResponseDTO getBabySittersResponseDTO (MatchBabySitterResponseDTO dto, double avg, List<Review> rvs) {
        return MatchBabySitterResponseDTO.builder()
                .id(dto.id())
                .name(dto.name())
                .score(avg)
                .pay(dto.pay())
                .old(dto.old())
                .profile(dto.profile())
                .gender(dto.gender())
                .reason(dto.reason())
                .tags(dto.tags())
                .reviews(rvs)
                .build();
    }

    private String getPromptMessage (@NotNull MatchBabySittersRequestDTO requestDTO, List<BabySitter> babySitters) {
        return """
               사용자의 요구사항에 가장 적절한 상위 3명의 베이비시터를 추천해줘. 응답 양식은 json 배열로 보내주고,
               각 베이비 시터를 추천하는 이유를 reason 필드에, 성격 혹은 성향을 나타내는 태그 3개를 tags 필드에 입력해서 각 객체에 포함해줘.
               베이비 시터 객체의 id, 기존 pay 값도 필수로 포함시켜줘. pay 값 변경하지마.
               """
                + "사용자 요구사항: "
                + requestDTO.content() + "\n\n"
                + "베이비시터 리스트: "
                + babySitters;
    }

    private String callGpt (List<ChatMessage> messages) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(GPT_3_5_TURBO_0301.getName())
                .messages(messages)
                .build();
        ChatCompletionResult result = openAiService.createChatCompletion(chatCompletionRequest);

        String gptResponse = result.getChoices().get(0).getMessage().getContent();
        System.out.println("gptResponse = " + gptResponse);
        return gptResponse;
    }

    private List<MatchBabySitterResponseDTO> jsonArrayToList (JSONArray array) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(array,
                        TypeFactory.defaultInstance().constructCollectionType(List.class, MatchBabySitterResponseDTO.class));
    }
}

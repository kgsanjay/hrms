package com.example.hrms.service;

import com.example.hrms.dto.CreateUpdateHolidayRequest;
import com.example.hrms.dto.HolidayDto;
import com.example.hrms.entity.Holiday;
import com.example.hrms.mapper.HolidayMapper;
import com.example.hrms.repository.HolidayRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayMapper holidayMapper;

    public HolidayService(HolidayRepository holidayRepository, HolidayMapper holidayMapper) {
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
    }

    public HolidayDto createHoliday(CreateUpdateHolidayRequest request) {
        Holiday holiday = new Holiday();
        holiday.setDate(request.getDate());
        holiday.setDescription(request.getDescription());
        return holidayMapper.toDto(holidayRepository.save(holiday));
    }

    public List<HolidayDto> getAllHolidays() {
        return holidayRepository.findAll().stream().map(holidayMapper::toDto).collect(Collectors.toList());
    }

    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }
}

package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.TherapyProgramBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapistDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapyProgramDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Therapist;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapyProgramBOImpl implements TherapyProgramBO {
    TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOTypes.THERAPIST);
    TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOTypes.PROGRAM);

    @Override
    public boolean saveProgram(TherapyProgramDTO dto) {
        Optional<Therapist> byId = therapistDAO.findById(dto.getTherapistId());
        if (byId.isEmpty()) return false;

        Therapist therapist = byId.get();

        TherapyProgram program = new TherapyProgram(
                dto.getProgramId(),
                dto.getName(),
                dto.getDurationInWeeks(),
                dto.getFee(),
                therapist,
                null
        );

        therapist.getPrograms().add(program);

        return therapyProgramDAO.save(program);
    }

    @Override
    public boolean updateProgram(TherapyProgramDTO dto) {
        Optional<Therapist> byId = therapistDAO.findById(dto.getTherapistId());
        if (byId.isEmpty()) return false;

        Therapist therapist = byId.get();

        TherapyProgram updatedProgram = new TherapyProgram(
                dto.getProgramId(),
                dto.getName(),
                dto.getDurationInWeeks(),
                dto.getFee(),
                therapist,
                null
        );

        return therapyProgramDAO.update(updatedProgram);
    }

    @Override
    public boolean deleteProgram(String id) {
        return therapyProgramDAO.delete(id);
    }

    @Override
    public String getNextProgramId() {
        Optional<String> lastId = therapyProgramDAO.getLastId();

        if (lastId.isPresent()) {
            String lastID = lastId.get();
            int numericPart = Integer.parseInt(lastID.substring(1));
            numericPart++;
            return String.format("TP%03d", numericPart);
        } else {
            return "TP001";
        }
    }

    @Override
    public List<TherapyProgramDTO> getPrograms() {
        List<TherapyProgram> all = therapyProgramDAO.getAll();
        List<TherapyProgramDTO> therapyProgramDTOS = new ArrayList<>();

        for (TherapyProgram program : all) {
            TherapyProgramDTO dto = new TherapyProgramDTO();
            dto.setProgramId(program.getProgramId());
            dto.setName(program.getName());
            dto.setDurationInWeeks(program.getDurationInWeeks());
            dto.setFee(program.getFee());
            dto.setTherapistId(program.getTherapist().getTherapistId());
            therapyProgramDTOS.add(dto);
        }
        return therapyProgramDTOS;
    }

    @Override
    public List<TherapistDTO> getTherapists() {
        List<Therapist> therapists = therapistDAO.getAll();
        List<TherapistDTO> therapistDTOS = new ArrayList<>();

        for (Therapist therapist : therapists) {
            TherapistDTO therapistDTO = new TherapistDTO();
            therapistDTO.setTherapistId(therapist.getTherapistId());
            therapistDTO.setName(therapist.getName());
            therapistDTO.setContactNumber(therapist.getContactNumber());
            therapistDTO.setSpecialization(therapist.getSpecialization());
            therapistDTOS.add(therapistDTO);
        }

        return therapistDTOS;
    }

    @Override
    public TherapyProgramDTO getProgram(String id) {
        Optional<TherapyProgram> byId = therapyProgramDAO.findById(id);
        if (byId.isPresent()) {
            TherapyProgram program = byId.get();
            TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO();
            therapyProgramDTO.setProgramId(program.getProgramId());
            therapyProgramDTO.setName(program.getName());
            therapyProgramDTO.setDurationInWeeks(program.getDurationInWeeks());
            therapyProgramDTO.setFee(program.getFee());
            therapyProgramDTO.setTherapistId(program.getTherapist().getTherapistId());

            return therapyProgramDTO;
        }
        return null;
    }

    @Override
    public TherapistDTO getTherapist(String id) {
        Optional<Therapist> byId = therapistDAO.findById(id);
        if (byId.isPresent()) {
            Therapist therapist = byId.get();
            TherapistDTO therapistDTO = new TherapistDTO();
            therapistDTO.setTherapistId(therapist.getTherapistId());
            therapistDTO.setName(therapist.getName());
            therapistDTO.setContactNumber(therapist.getContactNumber());
            therapistDTO.setSpecialization(therapist.getSpecialization());

            return therapistDTO;
        }
        return null;
    }
}

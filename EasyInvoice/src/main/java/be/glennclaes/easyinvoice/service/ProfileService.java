package be.glennclaes.easyinvoice.service;

import be.glennclaes.easyinvoice.io.ProfileRequest;
import be.glennclaes.easyinvoice.io.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);
}

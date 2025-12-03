package com.hotel.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a guest user in the hotel booking system.
 * A Guest can have a booking history, be marked as a returning guest,
 * and have a preferred payment method.
 */
public class Guest extends User {
	/** Immutable list backing: booking identifiers associated with this guest. */
	private List<String> bookingHistory;

	/** Flag indicating whether the guest is a returning customer. */
	private boolean returningGuest;

	/** Optional preferred payment method for the guest. */
	private PaymentMethod paymentMethod;

	/**
	 * Create a new Guest.
	 * <p>
	 * If {@code password} is null or empty a generated placeholder password is used
	 * (prefixed with {@code "guest-" + userId}) so that the underlying {@link User}
	 * constructor validation succeeds.
	 *
	 * @param userId   unique identifier for the guest (non-null, non-empty)
	 * @param name     guest's full name (non-null, non-empty)
	 * @param email    guest's email (non-null, valid format)
	 * @param phone    guest's phone (non-null, non-empty)
	 * @param password optional password; if null/empty a placeholder is used
	 * @throws IllegalArgumentException if required inputs are invalid (delegated to {@code User})
	 */
	public Guest(String userId, String name, String email, String phone, String password) {
		// Guests are often created/managed by staff and may not have a usable password for login.
		// Ensure a non-empty password is passed to the User constructor to satisfy its validation.
		super(userId, name, email, phone, (password == null || password.trim().isEmpty()) ? ("guest-" + userId) : password, UserRole.GUEST);
		this.bookingHistory = new ArrayList<>();
		this.returningGuest = false;
		this.paymentMethod = null;
	}

	/**
	 * Add a booking identifier to this guest's booking history.
	 * <p>
	 * Duplicate booking IDs are ignored.
	 *
	 * @param bookingId non-null, non-empty booking identifier
	 * @throws IllegalArgumentException if {@code bookingId} is null or empty
	 */
	public void addBooking(String bookingId) {
		validateInput(bookingId, "Booking ID");
		if (!bookingHistory.contains(bookingId)) {
			bookingHistory.add(bookingId);
		}
	}

	/**
	 * Returns a defensive copy of the booking history.
	 *
	 * @return list of booking IDs (never null)
	 */
	public List<String> getBookingHistory() {
		return new ArrayList<>(bookingHistory);
	}

	/**
	 * Check whether this guest is marked as a returning guest.
	 *
	 * @return true if returning guest, false otherwise
	 */
	public boolean isReturningGuest() {
		return returningGuest;
	}

	/**
	 * Mark or unmark this guest as a returning guest.
	 *
	 * @param returningGuest boolean flag
	 */
	public void setReturningGuest(boolean returningGuest) {
		this.returningGuest = returningGuest;
	}

	/**
	 * Get the preferred payment method for this guest, if any.
	 *
	 * @return the {@link PaymentMethod} or null if not set
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Set or clear the guest's preferred payment method.
	 *
	 * @param paymentMethod payment method to set, or null to clear
	 */
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Human readable permissions summary for guest users.
	 *
	 * @return short description of guest permissions
	 */
	@Override
	public String getPermissions() {
		return "Guest: View own bookings, request support";
	}

	/**
	 * Check whether the guest has the given permission token.
	 *
	 * @param permission permission token to check
	 * @return true for known guest permissions (e.g. {@code VIEW_BOOKINGS}, {@code REQUEST_SUPPORT}), false otherwise
	 */
	@Override
	public boolean hasPermission(String permission) {
		switch (permission) {
			case "VIEW_BOOKINGS":
			case "REQUEST_SUPPORT":
				return true;
			default:
				return false;
		}
	}

	/**
	 * Guests are equal when their {@code userId} values are equal.
	 *
	 * @param o object to compare
	 * @return true if same userId, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Guest)) return false;
		Guest guest = (Guest) o;
		return getUserId().equals(guest.getUserId());
	}

	/**
	 * Hash code based on {@code userId}.
	 *
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getUserId());
	}
}

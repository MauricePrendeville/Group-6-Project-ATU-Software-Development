package com.hotel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Guest extends User {
	private List<String> bookingHistory;
	private boolean returningGuest;
	private PaymentMethod paymentMethod;

	public Guest(String userId, String name, String email, String phone, String password) {
		// Guests are managed by staff and may not have a usable password for login.
		// Ensure a non-empty password is passed to the User constructor.
		super(userId, name, email, phone, (password == null || password.trim().isEmpty()) ? ("guest-" + userId) : password, UserRole.GUEST);
		this.bookingHistory = new ArrayList<>();
		this.returningGuest = false;
		this.paymentMethod = null;
	}

	public void addBooking(String bookingId) {
		validateInput(bookingId, "Booking ID");
		if (!bookingHistory.contains(bookingId)) {
			bookingHistory.add(bookingId);
		}
	}

	public List<String> getBookingHistory() {
		return new ArrayList<>(bookingHistory);
	}

	public boolean isReturningGuest() {
		return returningGuest;
	}

	public void setReturningGuest(boolean returningGuest) {
		this.returningGuest = returningGuest;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public String getPermissions() {
		return "Guest: View own bookings, request support";
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Guest)) return false;
		Guest guest = (Guest) o;
		return getUserId().equals(guest.getUserId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId());
	}
}

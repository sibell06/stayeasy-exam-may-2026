document.addEventListener('DOMContentLoaded', function () {

    const today = new Date().toISOString().split('T')[0];
    const checkIn = document.getElementById('checkIn');
    const checkOut = document.getElementById('checkOut');
    const guests = document.getElementById('guests');

    if (!checkIn || !checkOut) return;

    // Set today as minimum date for check-in
    checkIn.min = today;
    checkOut.min = today;

    // Clear default values so fields appear empty
    if (!checkIn.value) checkIn.value = '';
    if (!checkOut.value) checkOut.value = '';
    if (guests && guests.value === '0') guests.value = '';

    // When check-in changes, set check-out min to next day
    checkIn.addEventListener('change', function () {
        if (this.value) {
            const nextDay = new Date(this.value);
            nextDay.setDate(nextDay.getDate() + 1);
            checkOut.min = nextDay.toISOString().split('T')[0];
            if (checkOut.value && checkOut.value <= this.value) {
                checkOut.value = '';
            }
        }
    });
});
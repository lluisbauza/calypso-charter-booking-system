const availableDatesSet = new Set(availableDates);

let currentDate = new Date();

let currentYear = currentDate.getFullYear();
let currentMonth = currentDate.getMonth();

function renderCalendar() {
    const calendar = document.getElementById("calendar");
    const monthTitle = document.getElementById("monthTitle");

    calendar.innerHTML = "";

    const monthName = new Date(
        currentYear,
        currentMonth
    ).toLocaleString("es", {
        month: "long",
        year: "numeric"
    });

    monthTitle.textContent = monthName;

    const daysInMonth = new Date(
        currentYear,
        currentMonth + 1,
        0
    ).getDate();

    for (let day = 1; day <= daysInMonth; day++) {
        const button = document.createElement("button");

        button.textContent = day;
        button.type = "button";

        const month = String(currentMonth + 1).padStart(2, "0");
        const formattedDay = String(day).padStart(2, "0");

        const dateString =
            `${currentYear}-${month}-${formattedDay}`;

        if (availableDatesSet.has(dateString)) {
            button.disabled = false;

            button.addEventListener("click", () => {
                window.location.href =
                    `/boats?date=${dateString}`;
            });

        } else {
            button.disabled = true;
        }

        calendar.appendChild(button);
    }
}

document.getElementById("nextMonth")
    .addEventListener("click", () => {
        currentMonth++;

        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }

        renderCalendar();
    });

document.getElementById("previousMonth")
    .addEventListener("click", () => {
        currentMonth--;

        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }

        renderCalendar();
    });

renderCalendar();
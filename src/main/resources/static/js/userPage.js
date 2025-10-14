document.addEventListener('DOMContentLoaded', function () {
    loadUser();
});

async function loadUser() {
    const response = await fetch('/api/user/current');
    const user = await response.json();
    renderHead(user);
    renderTable(user);
}

function renderTable(user) {
    const tbody = document.getElementById('userTable');
    tbody.innerHTML = `
        <tr class="border-bottom 0">
            <td class="border-0 py-3">${user.id}</td>
            <td class="border-0 py-3">${user.firstName}</td>
            <td class="border-0 py-3">${user.lastName}</td>
            <td class="border-0 py-3">${user.age}</td>
            <td class="border-0 py-3">${user.email}</td>
            <td class="border-0 py-3">${user.roles.map(r => r.authority).join(' ')}</td>
        </tr>
    `
}

function renderHead(user) {
    document.getElementById('username').textContent = user.email;
    document.getElementById('roles').textContent = user.roles.map(role => role.authority).join(' ');
    if (user.roles.some(role => role.authority === 'ADMIN')) {
        const panelAdmin = document.getElementById('forAdmin');
        panelAdmin.insertAdjacentHTML('afterbegin', `<a href="/admin/"
            class="btn btn-outline-primary rounded-1 py-2 w-100 border-0 fs-6 text-start">
            Admin
        </a>`);
    }
}

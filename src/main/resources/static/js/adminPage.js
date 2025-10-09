const API_BASE_URL = '/api/admin/';
const modalEdit = new bootstrap.Modal(document.getElementById("editModal"));
const modalDelete = new bootstrap.Modal(document.getElementById("deleteModal"));

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers: async () => await fetch(API_BASE_URL),
    findOneUser: async (id) => await fetch(`${API_BASE_URL}${id}`),
    addNewUser: async (user) => await fetch(API_BASE_URL, {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`${API_BASE_URL}${id}`, {
        method: 'PUT',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`${API_BASE_URL}${id}`, {
        method: 'DELETE',
        headers: userFetchService.head
    }),
    findAllRoles: async () => await fetch(API_BASE_URL + 'roles'),
    findCurrentUser: async () => await fetch(API_BASE_URL + 'current')
}

const errorHandler = {
    clearErrors(formType = 'add') {
        document.querySelectorAll(`.invalid-feedback`).forEach(el => {
            el.style.display = 'none';
        });
        document.querySelectorAll('input').forEach(input => {
            input.classList.remove('is-invalid');
        });
    },

    showError(fieldName, errorMessage, formType = 'add') {
        const inputId = formType === 'add' ? fieldName : `${fieldName}ModuleEdit`;
        const errorId = formType === 'add' ? `${fieldName}AddError` : `${fieldName}EditError`;

        const input = document.getElementById(inputId);
        const error = document.getElementById(errorId);

        if (input && error) {
            input.classList.add('is-invalid');
            error.textContent = errorMessage;
            error.style.display = 'block';
        }
    },

    displayErrors(errors, formType = 'add') {
        this.clearErrors(formType);
        if (errors.errors) {
            Object.entries(errors.errors).forEach(([field, message]) => {
                this.showError(field, message, formType);
            });
        }
    }
};

document.addEventListener('DOMContentLoaded', function () {
    loadUsers();
    fillSelectForAdd();
});

async function renderHead() {
    const response = await userFetchService.findCurrentUser();
    const user = await response.json()
    document.getElementById('username').textContent = user.email;
    document.getElementById('roles').textContent = user.roles.map(role => role.authority).join(' ');
}

async function loadUsers() {
    const response = await userFetchService.findAllUsers();
    const users = await response.json();
    renderHead();
    renderTable(users);
}

function renderTable(users) {
    const tbody = document.getElementById('usersTable');
    tbody.innerHTML = users.map((user, index) => `
        <tr class="border-bottom ${index === users.length - 1 ? 'border-bottom-0' : ''}">
            <td class="border-0 py-3">${user.id}</td>
            <td class="border-0 py-3">${user.firstName}</td>
            <td class="border-0 py-3">${user.lastName}</td>
            <td class="border-0 py-3">${user.age}</td>
            <td class="border-0 py-3">${user.email}</td>
            <td class="border-0 py-3">${user.roles.map(r => r.authority).join(' ')}</td>
            <td class="border-0 py-3">
                <button class="btn btn-info btn-edit" data-user-id="${user.id}">Edit</button>
            </td>
            <td class="border-0 py-3">
                <button class="btn btn-danger btn-delete" data-user-id="${user.id}">Delete</button>
            </td>
        </tr>
    `).join('');
    addClick();
}

function addClick() {
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.onclick = () => openEditModal(btn.dataset.userId);
    });
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.onclick = () => openDeleteModal(btn.dataset.userId);
    });
}

async function openEditModal(userId) {
    errorHandler.clearErrors('edit');
    const response = await userFetchService.findOneUser(userId);
    const user = await response.json();

    document.getElementById('idModuleEdit').value = user.id;
    document.getElementById('firstNameModuleEdit').value = user.firstName;
    document.getElementById('lastNameModuleEdit').value = user.lastName;
    document.getElementById('ageModuleEdit').value = user.age;
    document.getElementById('emailModuleEdit').value = user.email;
    document.getElementById('passwordModuleEdit').value = user.password;
    await fillRolesSelect(user.roles, 'roleModuleEdit');

    const editButton = document.getElementById('buttonEdit');
    editButton.replaceWith(editButton.cloneNode(true));
    document.getElementById('buttonEdit').onclick = (e) => updateUser(userId, e);
    modalEdit.show();
}

async function openDeleteModal(userId) {
    const response = await userFetchService.findOneUser(userId);
    const user = await response.json();

    document.getElementById('idModuleDelete').value = user.id;
    document.getElementById('firstNameModuleDelete').value = user.firstName;
    document.getElementById('lastNameModuleDelete').value = user.lastName;
    document.getElementById('ageModuleDelete').value = user.age;
    document.getElementById('emailModuleDelete').value = user.email;
    await fillRolesSelect(user.roles, 'roleModuleDelete');

    const deleteButton = document.getElementById('buttonDelete');
    deleteButton.replaceWith(deleteButton.cloneNode(true));
    document.getElementById('buttonDelete').addEventListener('click', async (event) => {
        event.preventDefault();
        const response = await userFetchService.deleteUser(userId);
        if (response.ok) {
            await loadUsers();
            modalDelete.hide();
        }
    });
    modalDelete.show();
}

async function fillRolesSelect(userRoles, selectId) {
    const response = await userFetchService.findAllRoles();
    const allRoles = await response.json();
    const select = document.getElementById(selectId);

    select.innerHTML = allRoles.map(role => `
        <option value="${role.authority}" ${userRoles.some(r => r.authority === role.authority) ? 'selected' : ''}>
            ${role.authority}
        </option>
    `).join('');
}

async function getSelectedRoles(selectId) {
    const select = document.getElementById(selectId);
    const selected = Array.from(select.selectedOptions).map(opt => opt.value);
    const response = await userFetchService.findAllRoles();
    const allRoles = await response.json();
    return allRoles.filter(role => selected.includes(role.authority));
}

async function getFormData(type) {
    const base = type === 'add' ? '' : 'ModuleEdit';
    const userData = {
        firstName: document.getElementById(`firstName${base}`).value,
        lastName: document.getElementById(`lastName${base}`).value,
        age: document.getElementById(`age${base}`).value,
        email: document.getElementById(`email${base}`).value,
        password: document.getElementById(`password${base}`).value,
        roles: await getSelectedRoles(type === 'add' ? 'rolesSelect' : 'roleModuleEdit')
    };
    if (base === 'ModuleEdit') {
        userData.id = document.getElementById(`id${base}`).value;
    }
    return userData;
}

async function updateUser(userId, event) {
    event.preventDefault();
    try {
        errorHandler.clearErrors('edit');
        const userData = await getFormData('edit');
        const response = await userFetchService.updateUser(userData, userId);

        if (!response.ok) {
            const errors = await response.json();
            throw errors;
        }

        await loadUsers();
        modalEdit.hide();
    } catch (errors) {
        errorHandler.displayErrors(errors, 'edit');
    }
}

document.getElementById('btnForAddNewUser').onclick = async function (e) {
    e.preventDefault();
    try {
        errorHandler.clearErrors('add');
        const userData = await getFormData('add');
        const response = await userFetchService.addNewUser(userData);

        if (!response.ok) {
            const errors = await response.json();
            throw errors;
        }
        await loadUsers();
        document.querySelector('[data-bs-target="#table"]').click();
    } catch (errors) {
        errorHandler.displayErrors(errors, 'add');
    }
};
document.querySelector('[data-bs-target="#table"]').addEventListener('click', () => {
    document.querySelector('#add form').reset();
    errorHandler.clearErrors('add');
})

async function fillSelectForAdd() {
    const response = await userFetchService.findAllRoles();
    const allRoles = await response.json();
    document.getElementById('rolesSelect').innerHTML = allRoles.map(role =>
        `<option value="${role.authority}">${role.authority}</option>`
    ).join('');
}

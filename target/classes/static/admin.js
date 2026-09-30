const $ = id => document.getElementById(id);
const label = s => s.replace('_', ' ').toLowerCase().replace(/^./, c => c.toUpperCase());
let auth = sessionStorage.getItem('auth');

function el(tag, cls, text) {
  const n = document.createElement(tag);
  if (cls) n.className = cls;
  if (text !== undefined) n.textContent = text;
  return n;
}

async function api(path, opts = {}) {
  const res = await fetch(path, {
    ...opts,
    headers: { 'Content-Type': 'application/json', Authorization: auth, ...(opts.headers || {}) }
  });
  if (res.status === 401) { logout(); throw new Error('Wrong username or password.'); }
  if (!res.ok) throw new Error('Request failed (' + res.status + ').');
  return res.json();
}

function logout() {
  sessionStorage.removeItem('auth'); auth = null;
  $('login').classList.remove('hidden'); $('panel').classList.add('hidden'); $('logout').classList.add('hidden');
}

async function load() {
  const f = $('filter').value;
  const tickets = await api('/api/admin/tickets' + (f ? '?status=' + f : ''));
  $('login').classList.add('hidden'); $('panel').classList.remove('hidden'); $('logout').classList.remove('hidden');
  const list = $('list'); list.replaceChildren();
  if (!tickets.length) { list.append(el('p', 'sub', 'No tickets here yet.')); return; }
  tickets.forEach(t => list.append(card(t)));
}

function card(t) {
  const c = el('article', 'ticket ' + t.status);
  c.append(el('h3', '', `#${t.id} ${t.title}`));
  const meta = el('div', 'meta');
  meta.append(el('span', 'badge ' + t.status, label(t.status)), ' ',
              el('span', 'badge ' + t.priority, label(t.priority) + ' priority'),
              ` ${t.requesterName} (${t.requesterEmail}) on ${new Date(t.createdAt).toLocaleString()}`);
  c.append(meta, el('p', 'desc', t.description));

  const sl = el('label', '', 'Solution'); const ta = el('textarea'); ta.value = t.solution || '';
  ta.id = 'sol' + t.id; sl.htmlFor = ta.id;
  const stl = el('label', '', 'Status'); const sel = el('select'); sel.id = 'st' + t.id; stl.htmlFor = sel.id;
  ['OPEN', 'IN_PROGRESS', 'RESOLVED'].forEach(s => {
    const o = el('option', '', label(s)); o.value = s; if (s === t.status) o.selected = true; sel.append(o);
  });
  const btn = el('button', '', 'Save changes'); const note = el('span', 'meta', '');
  btn.addEventListener('click', async () => {
    try {
      await api('/api/admin/tickets/' + t.id, { method: 'PUT', body: JSON.stringify({ solution: ta.value, status: sel.value }) });
      await load();
    } catch (e) { note.textContent = ' ' + e.message; }
  });
  c.append(sl, ta, stl, sel, btn, note);
  return c;
}

$('loginForm').addEventListener('submit', async e => {
  e.preventDefault();
  auth = 'Basic ' + btoa($('user').value + ':' + $('pass').value);
  try { await load(); sessionStorage.setItem('auth', auth); $('loginMsg').classList.add('hidden'); }
  catch (err) { $('loginMsg').textContent = err.message; $('loginMsg').classList.remove('hidden'); }
});
$('filter').addEventListener('change', load);
$('logout').addEventListener('click', e => { e.preventDefault(); logout(); });
if (auth) load().catch(() => {});

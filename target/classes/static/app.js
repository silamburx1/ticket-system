const $ = id => document.getElementById(id);
const label = s => s.replace('_', ' ').toLowerCase().replace(/^./, c => c.toUpperCase());

function show(el, text, isErr) {
  el.textContent = text;
  el.className = 'msg' + (isErr ? ' err' : '');
}

$('ticketForm').addEventListener('submit', async e => {
  e.preventDefault();
  const body = {
    name: $('name').value, email: $('email').value, title: $('title').value,
    priority: $('priority').value, description: $('description').value
  };
  try {
    const res = await fetch('/api/tickets', {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
    });
    if (!res.ok) throw new Error('Check that every field is filled in and the email is valid.');
    const t = await res.json();
    show($('createMsg'), `Ticket #${t.id} created. Save this number to check its status.`);
    e.target.reset();
    $('tid').value = t.id; $('temail').value = body.email;
  } catch (err) { show($('createMsg'), err.message, true); }
});

$('trackForm').addEventListener('submit', async e => {
  e.preventDefault();
  const box = $('result');
  box.replaceChildren();
  try {
    const res = await fetch(`/api/tickets/${$('tid').value}?email=${encodeURIComponent($('temail').value)}`);
    if (!res.ok) throw new Error('No ticket found for that number and email.');
    const t = await res.json();
    const d = document.createElement('div');
    d.className = 'ticket ' + t.status;
    const h = document.createElement('h3'); h.textContent = `#${t.id} ${t.title}`;
    const b = document.createElement('span'); b.className = 'badge ' + t.status; b.textContent = label(t.status);
    const p = document.createElement('p'); p.className = 'desc'; p.textContent = t.description;
    d.append(h, b, p);
    const s = document.createElement('div'); s.className = 'solution';
    s.textContent = t.solution ? 'Solution: ' + t.solution : 'No solution yet. We are looking at it.';
    d.append(s);
    box.append(d);
  } catch (err) {
    const m = document.createElement('div'); m.className = 'msg err'; m.textContent = err.message; box.append(m);
  }
});

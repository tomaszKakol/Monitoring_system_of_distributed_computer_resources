import { mount } from '@vue/test-utils'
import sinon from 'sinon'
import ButtonBar from '@/components/elements/ButtonBar'


describe('ButtonBar', () => {
  let spy;
  let btn;

  beforeEach(() => {
    spy = sinon.spy()
    btn = mount(ButtonBar, {
      propsData: {
        icon: 'exit',
        handler: spy
      }
    })
  })


  it('handler function called when clicking', () => {
    sinon.assert.notCalled(spy)
    btn.trigger('click')
    sinon.assert.calledOnce(spy)
  })
})

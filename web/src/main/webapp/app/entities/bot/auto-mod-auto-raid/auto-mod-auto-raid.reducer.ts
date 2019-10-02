import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IAutoModAutoRaid, defaultValue } from 'app/shared/model/bot/auto-mod-auto-raid.model';

export const ACTION_TYPES = {
  FETCH_AUTOMODAUTORAID_LIST: 'autoModAutoRaid/FETCH_AUTOMODAUTORAID_LIST',
  FETCH_AUTOMODAUTORAID: 'autoModAutoRaid/FETCH_AUTOMODAUTORAID',
  CREATE_AUTOMODAUTORAID: 'autoModAutoRaid/CREATE_AUTOMODAUTORAID',
  UPDATE_AUTOMODAUTORAID: 'autoModAutoRaid/UPDATE_AUTOMODAUTORAID',
  DELETE_AUTOMODAUTORAID: 'autoModAutoRaid/DELETE_AUTOMODAUTORAID',
  RESET: 'autoModAutoRaid/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAutoModAutoRaid>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AutoModAutoRaidState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoModAutoRaidState = initialState, action): AutoModAutoRaidState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODAUTORAID_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODAUTORAID):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUTOMODAUTORAID):
    case REQUEST(ACTION_TYPES.UPDATE_AUTOMODAUTORAID):
    case REQUEST(ACTION_TYPES.DELETE_AUTOMODAUTORAID):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODAUTORAID_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODAUTORAID):
    case FAILURE(ACTION_TYPES.CREATE_AUTOMODAUTORAID):
    case FAILURE(ACTION_TYPES.UPDATE_AUTOMODAUTORAID):
    case FAILURE(ACTION_TYPES.DELETE_AUTOMODAUTORAID):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODAUTORAID_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODAUTORAID):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUTOMODAUTORAID):
    case SUCCESS(ACTION_TYPES.UPDATE_AUTOMODAUTORAID):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUTOMODAUTORAID):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/auto-mod-auto-raids';

// Actions

export const getEntities: ICrudGetAllAction<IAutoModAutoRaid> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUTOMODAUTORAID_LIST,
  payload: axios.get<IAutoModAutoRaid>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAutoModAutoRaid> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOMODAUTORAID,
    payload: axios.get<IAutoModAutoRaid>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAutoModAutoRaid> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUTOMODAUTORAID,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAutoModAutoRaid> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUTOMODAUTORAID,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAutoModAutoRaid> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUTOMODAUTORAID,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
